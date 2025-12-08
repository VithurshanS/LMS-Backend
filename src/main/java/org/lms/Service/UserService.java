package org.lms.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import org.keycloak.representations.idm.UserRepresentation;
import org.lms.Dto.*;
import org.lms.Model.Department;
import org.lms.Model.UserRole;
import org.lms.Repository.DepartmentRepository;
import org.lms.Repository.LecturerRepository;
import org.lms.Repository.StudentRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService {
    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String keycloakUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @Inject
    JsonWebToken jwt;


    @Inject
    LecturerService lecturerService;

    @Inject
    DepartmentRepository deptrepo;

    @Inject
    ObjectMapper mapper;

    @Inject
    StudentService studentService;

    @Inject
    AdminService adminService;

    @Inject
    Keycloak keycloak;

    @Inject
    LecturerRepository lectRepo;

    @Inject
    StudentRepository studRepo;



    private static final String CLIENT_ID = "lms-iam";

    public UserRole matchRole(String textrole){
        if(textrole.equals("lecturer")){
            return UserRole.LECTURER;
        } else if (textrole.equals("admin")) {
            return UserRole.ADMIN;

        } else {return UserRole.STUDENT;}
    }

    public String userIdfromToken(){
        return jwt.getSubject();
    }

    public UserResponseDto getProfileFromToken(){
        String userId = userIdfromToken();
        UserDetailDto user = fetchUserDetail(UUID.fromString(userId));
        if (user.clientRole.contains("admin")){
            return adminService.getAdminDetails(UUID.fromString(userId));
        } else if (user.clientRole.contains("lecturer")) {
            return lecturerService.getLecturerDetails(UUID.fromString(userId));

        }else {
            return studentService.getStudentDetails(UUID.fromString(userId));
        }

    }


    private CredentialRepresentation prepareCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private UserRepresentation createIAMUser(RegistrationRequestDto userDto) {
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




    public UserDetailDto fetchUserDetail(UUID userId) {
        UsersResource ur = keycloak.realm("ironone").users();
        RolesResource rr = keycloak.realm("ironone").roles();

        try {

            UserRepresentation userRep = ur.get(userId.toString()).toRepresentation();
            String clientId = keycloak.realm("ironone").clients()
                    .findByClientId("lms-iam").get(0).getId();

            List<String> clientRoles = ur.get(userId.toString())
                    .roles()
                    .clientLevel(clientId)
                    .listEffective()
                    .stream()
                    .map(RoleRepresentation::getName)
                    .toList();



            UserDetailDto dto = new UserDetailDto();
            dto.id = UUID.fromString(userRep.getId());
            dto.userName = userRep.getUsername();
            dto.email = userRep.getEmail();
            dto.firstName = userRep.getFirstName();
            dto.lastName = userRep.getLastName();
            dto.isActive = userRep.isEnabled();
            dto.emailVerified = userRep.isEmailVerified();
            dto.clientRole = clientRoles;


            return dto;

        } catch (NotFoundException e) {
            throw new RuntimeException("User not found with ID: " + userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user details: " + e.getMessage());
        }
    }



    @Transactional
    public void saveUserInDB(UUID userId, UserRole role, UUID departmentId) {
        try {
            Department department = null;
            if (departmentId != null) {
                department = deptrepo.findById(departmentId);
            }
            
            if(role == UserRole.LECTURER){
                lecturerService.createLecturer(userId, department);
            }else if(role == UserRole.ADMIN){
                adminService.createAdmin(userId);
            }else{
                studentService.createStudent(userId, department);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to save user in local database: " + e.getMessage(), e);
        }
    }


    @Transactional
    public Response registerUser(RegistrationRequestDto userDto, String realm) {

        if (!validRole(userDto.role)) {
            return Response.status(400).entity("Role is not acceptable").build();
        }

        UserRepresentation userPackage = createIAMUser(userDto);
        UsersResource usersResource = keycloak.realm(realm).users();

        try {
            Response res = usersResource.create(userPackage);

            if (res.getStatus() == 201) {
                String userId = usersResource.search(userDto.username).get(0).getId();
                boolean roleAssigned = assignRole(usersResource, userId, realm, userDto.role, CLIENT_ID);

                if (roleAssigned) {
                    try {
                        saveUserInDB(UUID.fromString(userId), matchRole(userDto.role), userDto.departmentId);
                        return Response.status(201).entity("User registered successfully").build();

                    }catch (Exception e){
                        return Response.status(400).entity("User registered successfully but cannot create local user" + e.toString()).build();
                    }


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





    public Response loginUser(LoginRequestDto credentials) {
        String openidUrl = keycloakUrl+"/protocol/openid-connect/token";
        String requestBody = "client_id="+clientId+ "&client_secret="+clientSecret + "&username=" + credentials.username + "&password=" + credentials.password + "&grant_type=password";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(openidUrl)).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        try{
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(res.statusCode()==200){
                return Response.status(200).entity(mapper.readValue(res.body(), LoginResponceDto.class)).build();
            }else{
                return Response.status(400).entity("Authendication failed"+res.body().toString()).build();
            }
        }catch (Exception e){
            return Response.status(500).entity("internal server error"+e.toString()).build();

        }

    }

    public Response approveUser(String lecturerId){
//        UUID userId = lecturerService.getLecturerDetails(userId)
        UUID userId = lectRepo.findById(UUID.fromString(lecturerId)).getUserId();
        UsersResource ur = keycloak.realm("ironone").users();
        try{
            UserRepresentation user = ur.get(userId.toString()).toRepresentation();

            user.setEnabled(true);
            user.setEmailVerified(true); //email verified if not then user is not approved

            ur.get(userId.toString()).update(user);
            return Response.ok("lecturer approved").build();
        } catch (NotFoundException e) {
            return Response.status(404).entity("User ID not found. Did you send a username instead of a UUID?").build();
        } catch (Exception e){
            return Response.status(400).entity(e.toString()).build();
        }
    }

    public void patchUser(String id, UserResponseDto update){
        UsersResource ur = keycloak.realm("ironone").users();
        UserRepresentation user = ur.get(id).toRepresentation();
        if (update.firstName!=null){
            user.setFirstName(update.firstName);
        }
        if(update.lastName!=null){
            user.setLastName(update.lastName);
        }

        user.setEnabled(update.isActive);
        ur.get(id).update(user);
    }

    public void controllUserAccess(String id, String choice, String role){
        String userID;
        if(role.equals("student")){
            userID = studRepo.findById(UUID.fromString(id)).getUserId().toString();
        } else if (role.equals("lecturer")) {
            userID = lectRepo.findById(UUID.fromString(id)).getUserId().toString();
        } else{
            throw new NotFoundException("user cannot be found");
        }


        if(choice.toLowerCase().equals("ban")){ // here i need to add a check to see the user is already banned or not
            patchUser(userID,new UserResponseDto(false));
        } else if (choice.toLowerCase().equals("unban")) {
            patchUser(userID,new UserResponseDto(true));
        }
    }

    private boolean validRole(String role) {
        return (role.equals("lecturer") ||role.equals("admin") || role.equals("student"));
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