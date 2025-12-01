package org.lms.User;

import org.lms.Model.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;

public interface User {

    UUID getId();
    String getEmail();
    String getUsername();
    String getFirstName();
    String getLastName();
    String getFullName();
    UserRole getRole();
    boolean isActive();
    boolean isApproved();
    void setRole(UserRole role);
    void setActive(boolean active);
    void setApproved(boolean approved);
}