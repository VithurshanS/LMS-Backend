package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.lms.Dto.LoginRequest;
import org.lms.Dto.RegistrationRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

@ApplicationScoped
public class UserService {
    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String keycloakUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @Inject
    Keycloak keycloak;

    private static final String CLIENT_ID = "lms-iam";

    public Response registerUser(RegistrationRequest userDto, String realm) {
        if (!validRole(userDto.role)) {
            return Response.status(400).entity("Role is not acceptable").build();
        }

        UserRepresentation userPackage = prepareUserRepresentation(userDto);
        UsersResource usersResource = keycloak.realm(realm).users();

        try {
            Response res = usersResource.create(userPackage);

            if (res.getStatus() == 201) {
                String userId = usersResource.search(userDto.username).get(0).getId();
                boolean roleAssigned = assignRole(usersResource, userId, realm, userDto.role, CLIENT_ID);

                if (roleAssigned) {
                    return Response.status(201).entity("User registered successfully").build();
                } else {
                    return Response.status(500).entity("User created but role assignment failed").build();
                }
            } else {
                return Response.status(res.getStatus()).entity("Failed to create user").build();
            }
        } catch (Exception e) {
            return Response.status(500).entity("Internal server error").build();
        }
    }

    public Response loginUser(LoginRequest credentials) {
        String openidUrl = keycloakUrl+"/protocol/openid-connect/token";
        String requestBody = "client_id="+clientId+ "&client_secret="+clientSecret + "&username=" + credentials.username + "&password=" + credentials.password + "&grant_type=password";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(openidUrl)).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        try{
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(res.statusCode()==200){
                return Response.status(200).entity(res.body()).build();
            }else{
                return Response.status(400).entity("Authendication failed"+res.body().toString()).build();
            }
        }catch (Exception e){
            return Response.status(500).entity("internal server error"+e.toString()).build();

        }

    }

    public Response approveLecturer(String userId){
        UsersResource ur = keycloak.realm("ironone").users();
        try{
            // This line WILL FAIL if userId is "shan"
            UserRepresentation user = ur.get(userId).toRepresentation();

            user.setEnabled(true);
            user.setEmailVerified(true); // Mark email as verified so they don't get prompted

            ur.get(userId).update(user);
            return Response.ok("lecturer approved").build();
        } catch (NotFoundException e) {
            return Response.status(404).entity("User ID not found. Did you send a username instead of a UUID?").build();
        } catch (Exception e){
            return Response.status(400).entity(e.toString()).build();
        }
    }

    private boolean validRole(String role) {
        return (role.equals("lecturer") || role.equals("student"));
    }

    private UserRepresentation prepareUserRepresentation(RegistrationRequest userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.username);
        user.setEmail(userDto.email);
        user.setFirstName(userDto.firstName);
        user.setLastName(userDto.lastName);
        if(userDto.role.equals("lecturer")){
            user.setEnabled(false);
        }else{
            user.setEnabled(true);
        }

        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(prepareCredential(userDto.password)));
        return user;
    }

    private CredentialRepresentation prepareCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private boolean assignRole(UsersResource ur, String userId, String realmName, String role, String clientName) {
        try {
            String clientUuid = keycloak.realm(realmName)
                    .clients()
                    .findByClientId(clientName)
                    .get(0)
                    .getId();

            RoleRepresentation roleRep = keycloak.realm(realmName)
                    .clients()
                    .get(clientUuid)
                    .roles()
                    .get(role)
                    .toRepresentation();

            ur.get(userId)
                    .roles()
                    .clientLevel(clientUuid)
                    .add(Collections.singletonList(roleRep));

            return true;
        } catch (Exception e) {
            return false;
        }
    }


}