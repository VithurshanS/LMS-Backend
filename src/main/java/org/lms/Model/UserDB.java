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
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public UserDB(){}

    public UserDB(String firstName, String lastName, String email, UserRole role, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = password;
        this.active = true;
        this.approved = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Setters
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
    
    // Getters - User interface implementation
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
    
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // Legacy method for backward compatibility
    @Deprecated
    public String getName() {
        return getFullName();
    }
    
    @Deprecated
    public void setName(String name) {
        if (name != null && name.contains(" ")) {
            String[] parts = name.split(" ", 2);
            this.firstName = parts[0];
            this.lastName = parts.length > 1 ? parts[1] : "";
        } else {
            this.firstName = name;
            this.lastName = "";
        }
    }
    public static UserRole matchRole(String textrole){
        if(textrole.equals("lecturer")){
            return UserRole.LECTURER;
        } else if (textrole.equals("admin")) {
            return UserRole.ADMIN;

        } else {return UserRole.STUDENT;}
    }




}
