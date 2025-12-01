package org.lms.Model;

import org.keycloak.representations.idm.UserRepresentation;
import org.lms.User.User;
import org.lms.Model.UserRole;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.Map;

public class UserIAM implements User {

    private final UserRepresentation userRepresentation;
    private UserRole role;

    public UserIAM(UserRepresentation userRepresentation) {
        this.userRepresentation = userRepresentation;
        this.role = extractRoleFromKeycloak();
    }




    @Override
    public UUID getId() {
        return UUID.fromString(userRepresentation.getId());
    }

    @Override
    public String getEmail() {
        return userRepresentation.getEmail();
    }

    @Override
    public String getUsername() {
        return userRepresentation.getUsername();
    }

    @Override
    public String getFirstName() {
        return userRepresentation.getFirstName();
    }

    @Override
    public String getLastName() {
        return userRepresentation.getLastName();
    }

    @Override
    public String getFullName() {
        String firstName = getFirstName() != null ? getFirstName() : "";
        String lastName = getLastName() != null ? getLastName() : "";
        return (firstName + " " + lastName).trim();
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public boolean isActive() {
        return userRepresentation.isEnabled() != null ? userRepresentation.isEnabled() : false;
    }

    @Override
    public boolean isApproved() {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.containsKey("approved")) {
            List<String> approvedValues = attributes.get("approved");
            return approvedValues != null && !approvedValues.isEmpty() && "true".equals(approvedValues.get(0));
        }
        return isActive();
    }


    @Override
    public void setRole(UserRole role) {
        this.role = role;
        // Update Keycloak user attributes
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new java.util.HashMap<>();
            userRepresentation.setAttributes(attributes);
        }
        attributes.put("role", List.of(role.toString()));
    }

    @Override
    public void setActive(boolean active) {
        userRepresentation.setEnabled(active);
    }

    @Override
    public void setApproved(boolean approved) {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new java.util.HashMap<>();
            userRepresentation.setAttributes(attributes);
        }
        attributes.put("approved", List.of(String.valueOf(approved)));
    }
    public UserRepresentation getUserRepresentation() {

        return userRepresentation;
    }

    private UserRole extractRoleFromKeycloak() {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.containsKey("role")) {
            List<String> roleValues = attributes.get("role");
            if (roleValues != null && !roleValues.isEmpty()) {
                try {
                    return UserRole.valueOf(roleValues.get(0).toUpperCase());
                } catch (Exception e) {
                }
            }
        }

        return UserRole.STUDENT;
    }

}