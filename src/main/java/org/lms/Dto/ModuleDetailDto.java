package org.lms.Dto;

import org.lms.Model.Department;

import java.util.UUID;

public class ModuleDetailDto {

    public UUID moduleId;
    public String moduleCode;
    public String name;
    public int enrollmentLimit;

    public Department department;

    public LecturerDetailDto lecturerDetail;
    public AdminDetailDto createdBy;

}