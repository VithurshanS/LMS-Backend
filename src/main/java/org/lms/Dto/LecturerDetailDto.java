package org.lms.Dto;

import org.lms.Model.Department;

import java.util.UUID;

public class LecturerDetailDto {
        public UUID lecturerId;
        public UserDetailDto lecturerUserDetail;
        public Department department;
}