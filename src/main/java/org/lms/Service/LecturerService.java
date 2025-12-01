package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jose4j.jwk.Use;
import org.lms.Dto.LecturerDetailDto;
import org.lms.Dto.UserDetailDto;
import org.lms.Model.Department;
import org.lms.Model.Lecturer;
import org.lms.Model.Module;
import org.lms.Repository.LecturerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LecturerService {
    @Inject
    LecturerRepository lectRepo;

    @Inject
    UserService userService;

    public void createLecturer(UUID userId, Department dept){
        lectRepo.persist(new Lecturer(userId,dept));
    }

    public List<Module> getAssignedModules(UUID lecturerId){
        return lectRepo.findById(lecturerId).getTeachingModules();
    }

    public LecturerDetailDto getLecturerDetails(UUID lecturerId){
        LecturerDetailDto dto = new LecturerDetailDto();
        Lecturer lect = lectRepo.findById(lecturerId);
        UserDetailDto userDetail = userService.fetchUserDetail(lect.getUserId());
        dto.department = lect.getDepartment();
        dto.lecturerId = lect.getId();
        dto.lecturerUserDetail = userDetail;
        return dto;

    }

    public List<LecturerDetailDto> getAllLecturers() {
        List<Lecturer> lecturers = lectRepo.listAll();
        List<LecturerDetailDto> output = new ArrayList<>();
        for (Lecturer i : lecturers) {
            LecturerDetailDto dto = new LecturerDetailDto();
            dto.lecturerId = i.getId();
            dto.department = i.getDepartment();
            try {
                dto.lecturerUserDetail = userService.fetchUserDetail(i.getUserId());
            } catch (Exception e) {
                System.out.println("User not found in Keycloak: " + i.getUserId());
                dto.lecturerUserDetail = null;
            }
            output.add(dto);
        }
        return output;
    }
}
