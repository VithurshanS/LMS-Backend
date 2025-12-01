package org.lms.User;

import org.lms.Model.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Common interface for user operations across different implementations
 * (database entities and IAM providers like Keycloak)
 */
public interface User {
    
    /**
     * Get the unique identifier for the user
     * @return UUID of the user
     */
    UUID getId();
    
    /**
     * Get the user's email address
     * @return email address
     */
    String getEmail();
    
    /**
     * Get the username for login
     * @return username
     */
    String getUsername();
    
    /**
     * Get the user's first name
     * @return first name
     */
    String getFirstName();
    
    /**
     * Get the user's last name
     * @return last name
     */
    String getLastName();
    
    /**
     * Get the user's full name
     * @return full name (first name + last name)
     */
    String getFullName();
    
    /**
     * Get the user's role
     * @return UserRole enum value
     */
    UserRole getRole();
    
    /**
     * Check if the user account is active/enabled
     * @return true if account is active, false otherwise
     */
    boolean isActive();
    
    /**
     * Check if the user is approved (for systems requiring approval)
     * @return true if approved, false otherwise
     */
    boolean isApproved();
    
    /**
     * Get the timestamp when the user was created
     * @return creation timestamp
     */
    LocalDateTime getCreatedAt();
    
    /**
     * Get the timestamp when the user was last updated
     * @return last update timestamp
     */
    LocalDateTime getUpdatedAt();
    
    /**
     * Set the user's role
     * @param role the new role to assign
     */
    void setRole(UserRole role);
    
    /**
     * Set the user's active status
     * @param active true to activate, false to deactivate
     */
    void setActive(boolean active);
    
    /**
     * Set the user's approval status
     * @param approved true to approve, false to unapprove
     */
    void setApproved(boolean approved);
}