package org.lms.Dto;

import java.util.UUID;

public class ModuleCreateRequest {
    public String code;
    public String name;
    public int limit;
    public UUID departmentId;
    public UUID adminId;
}
