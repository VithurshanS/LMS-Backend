package org.lms.Model;

import org.keycloak.representations.idm.UserRepresentation;
import org.lms.User.User;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.Map;

/**
 * UserIAM implementation that wraps Keycloak UserRepresentation
 * for IAM-based user operations
 */
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
        Boolean enabled = userRepresentation.isEnabled();
        return enabled != null && enabled;
    }
    
    @Override
    public boolean isApproved() {
        // Check if user has "approved" attribute or if they have any assigned roles
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.containsKey("approved")) {
            List<String> approvedValues = attributes.get("approved");
            return approvedValues != null && !approvedValues.isEmpty() && "true".equals(approvedValues.get(0));
        }
        // Default: consider active users as approved
        return isActive();
    }
    
    @Override
    public LocalDateTime getCreatedAt() {
        Long createdTimestamp = userRepresentation.getCreatedTimestamp();
        if (createdTimestamp != null) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), java.time.ZoneId.systemDefault());
        }
        return LocalDateTime.now(); // fallback
    }
    
    @Override
    public LocalDateTime getUpdatedAt() {
        // Keycloak doesn't have a standard updatedAt field, check custom attributes
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.containsKey("updatedAt")) {
            List<String> updatedValues = attributes.get("updatedAt");
            if (updatedValues != null && !updatedValues.isEmpty()) {
                try {
                    return LocalDateTime.parse(updatedValues.get(0));
                } catch (Exception e) {
                    // ignore parsing errors
                }
            }
        }
        return getCreatedAt(); // fallback to creation time
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
    
    /**
     * Get the underlying Keycloak UserRepresentation
     * @return the wrapped UserRepresentation
     */
    public UserRepresentation getUserRepresentation() {
        return userRepresentation;
    }
    
    /**
     * Extract role from Keycloak user attributes or realm roles
     * @return UserRole extracted from Keycloak data
     */
    private UserRole extractRoleFromKeycloak() {
        // First check custom attributes
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.containsKey("role")) {
            List<String> roleValues = attributes.get("role");
            if (roleValues != null && !roleValues.isEmpty()) {
                try {
                    return UserRole.valueOf(roleValues.get(0).toUpperCase());
                } catch (IllegalArgumentException e) {
                    // ignore invalid role values
                }
            }
        }
        
        // Check realm roles
        List<String> realmRoles = userRepresentation.getRealmRoles();
        if (realmRoles != null) {
            for (String realmRole : realmRoles) {
                try {
                    return UserRole.valueOf(realmRole.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // continue checking other roles
                }
            }
        }
        
        // Default to STUDENT if no role found
        return UserRole.STUDENT;
    }
    
    /**
     * Update the updatedAt timestamp in Keycloak attributes
     */
    public void updateTimestamp() {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) {
            attributes = new java.util.HashMap<>();
            userRepresentation.setAttributes(attributes);
        }
        attributes.put("updatedAt", List.of(LocalDateTime.now().toString()));
    }
}