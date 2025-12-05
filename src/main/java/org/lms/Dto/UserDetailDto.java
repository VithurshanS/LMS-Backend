package org.lms.Dto;

import java.util.List;
import java.util.UUID;

public class UserDetailDto {

    public UUID id;
    public UUID keycloakId;
    public String userName;
    public String email;
    public String firstName;
    public String lastName;
    public boolean isActive;
    public boolean emailVerified;
    public Long createdTimestamp;
    public List<String> clientRole;
}


