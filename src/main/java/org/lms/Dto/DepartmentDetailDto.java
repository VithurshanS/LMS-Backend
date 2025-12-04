package org.lms.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;



public class DepartmentDetailDto {
    @JsonProperty("id")
    public UUID departmentId;
    @JsonProperty("name")
    public String name;
}
