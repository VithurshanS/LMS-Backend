package org.lms.Model;

import org.lms.User.User;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;



@Entity
@Table(name = "users")
public class UserDB implements User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID id;

    @Column(nullable=false)
    private String firstName;
    
    @Column(nullable=false)
    private String lastName;
    
    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(unique = true, nullable= false)
    private String username;
    
    @Column(nullable = true)
    private String password;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @Column(nullable = false)
    private boolean approved = false;


    public UserDB(){}

    public UserDB(String firstName, String lastName, String email, String role, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = matchRole(role);
        this.username = username;
        this.password = password;
        this.active = true;
        this.approved = false;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isApproved() {
        return approved;
    }

    public static UserRole matchRole(String textrole){
        if(textrole.equals("lecturer")){
            return UserRole.LECTURER;
        } else if (textrole.equals("admin")) {
            return UserRole.ADMIN;

        } else {return UserRole.STUDENT;}
    }




}
