package org.lms.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.lms.Dto.AdminDetailDto;
import org.lms.Dto.UserDetailDto;
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
        adminRepo.persist(new Admin(userId));
    }

    public AdminDetailDto getAdminDetail(UUID adminId){
        AdminDetailDto dto = new AdminDetailDto();
        Admin admin = adminRepo.findById(adminId);
        UserDetailDto userDetail = userService.fetchUserDetail(admin.getUserId());
        dto.adminId = admin.getId();
        dto.adminUsername = userDetail.userName;
        return dto;
    }

}
