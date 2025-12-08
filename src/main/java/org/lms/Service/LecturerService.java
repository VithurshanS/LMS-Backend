package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.lms.Dto.LecturerDetailDto;
import org.lms.Dto.UserDetailDto;
import org.lms.Dto.UserResponseDto;
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

    @Transactional
    public void createLecturer(UUID userId, Department dept){
        Lecturer lecturer = new Lecturer(userId, dept);
        lectRepo.persist(lecturer);
    }

    public List<Module> getAssignedModules(UUID lecturerId){
        return lectRepo.findById(lecturerId).getTeachingModules();
    }

    public UserResponseDto getLecturerDetails(UUID userId){
        UserResponseDto user = new UserResponseDto();
        Lecturer lect = lectRepo.findByUserId(userId);
        user.departmentId = (lect.getDepartment() != null)
                ? lect.getDepartment().getId()
                : null;
        user.id = lect.getId();
        UserDetailDto userDetail;
        try {
            userDetail = userService.fetchUserDetail(lect.getUserId());
        } catch (Exception e) {
            System.out.println("User not found in Keycloak: " + lect.getUserId());
            return user;

        }
        user.username = userDetail.userName;
        user.email = userDetail.email;
        user.firstName = userDetail.firstName;
        user.lastName = userDetail.lastName;
        user.role = userDetail.clientRole.get(0).toUpperCase();
        user.isActive = userDetail.isActive;
        return user;


    }

    public UserResponseDto getLecturerDetails2(UUID lecturerId){
        UserResponseDto user = new UserResponseDto();
        Lecturer lect = lectRepo.findById(lecturerId);
        user.departmentId = (lect.getDepartment() != null)
                ? lect.getDepartment().getId()
                : null;
        user.id = lect.getId();
        UserDetailDto userDetail;
        try {
            userDetail = userService.fetchUserDetail(lect.getUserId());
        } catch (Exception e) {
            System.out.println("User not found in Keycloak: " + lect.getUserId());
            return user;

        }
        user.username = userDetail.userName;
        user.email = userDetail.email;
        user.firstName = userDetail.firstName;
        user.lastName = userDetail.lastName;
        user.role = userDetail.clientRole.get(0).toUpperCase();
        user.isActive = userDetail.isActive;
        return user;


    }

    public List<UserResponseDto> getLecturerDetailsbyDepartmentId(UUID departmentId) {

        List<Lecturer> lecturers = lectRepo.findByDepartmentId(departmentId);
//        if(lecturers.size()==0){
//            throw new RuntimeException("you repo problem");
//        }

        List<UserResponseDto> output = new ArrayList<>();
        for (Lecturer lecturer : lecturers) {
            UserResponseDto dto = getLecturerDetails2(lecturer.getId());
            output.add(dto);
        }
        return output;
    }


    public void patchLecturer(UserResponseDto update){
        try{
            String userId = userService.userIdfromToken();
            userService.patchUser(userId,update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserResponseDto> getAllLecturers2(){
        List<Lecturer> lecturers = lectRepo.listAll();
        List<UserResponseDto> output = new ArrayList<>();
        for (Lecturer i : lecturers) {
            UserResponseDto dto = new UserResponseDto();
            dto = getLecturerDetails2(i.getId());
            output.add(dto);
        }
        return output;
    }

    public List<LecturerDetailDto> getAllLecturers() {
        List<Lecturer> lecturers = lectRepo.listAll();
        List<LecturerDetailDto> output = new ArrayList<>();
        for (Lecturer i : lecturers) {
            LecturerDetailDto dto = new LecturerDetailDto();
            dto.lecturerId = i.getId();
            dto.departmentId = (i.getDepartment() != null)
                    ? i.getDepartment().getId()
                    : null;
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
