package org.lms.Dto;

import org.lms.Model.Department;

import java.util.UUID;

public class StudentDetailDto {
    public UUID lecturerId;
    public UserDetailDto lecturerUserDetail;
    public Department department;
}
