package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.lms.Dto.AdminDetailDto;
import org.lms.Dto.UserDetailDto;
import org.lms.Dto.UserResponseDto;
import org.lms.Model.Admin;
import org.lms.Repository.AdminRepository;

import java.util.UUID;

@ApplicationScoped
public class AdminService {
    @Inject
    AdminRepository adminRepo;

    @Inject
    UserService userService;

    public void createAdmin(UUID userId){
        Admin admin = new Admin(userId);
        adminRepo.persist(admin);
    }

    public AdminDetailDto getAdminDetail(UUID adminId){
        AdminDetailDto dto = new AdminDetailDto();
        Admin admin = adminRepo.findById(adminId);
        UserDetailDto userDetail = userService.fetchUserDetail(admin.getUserId());
        dto.adminId = admin.getId();
        dto.adminUsername = userDetail.userName;
        return dto;
    }

    public UserResponseDto getAdminDetails(UUID adminId){
        UserResponseDto user = new UserResponseDto();
        Admin lect = adminRepo.findByUserId(adminId);
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

}
