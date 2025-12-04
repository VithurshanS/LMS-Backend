package org.lms.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.lms.Model.Department;

import java.util.UUID;



public class ModuleDetailDto {
    @JsonProperty("id")
    public UUID moduleId;
    @JsonProperty("code")
    public String moduleCode;
    @JsonProperty("name")
    public String name;
    @JsonProperty("limit")
    public int enrollmentLimit;
    @JsonProperty("departmentId")
    public UUID departmentId;

    @JsonProperty("lecturerId")
    public UUID lecturerId;

    @JsonProperty("enrolledCount")
    public long enrolledCount;

    public UUID adminId;

}