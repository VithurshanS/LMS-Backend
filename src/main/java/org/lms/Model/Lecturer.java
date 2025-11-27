package org.lms.Model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name="lecturer")
public class Lecturer {

    @Id
    @GeneratedValue
    @Column(name = "lecturer_id")
    private UUID id;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy="lecturer")
    private List<Module> teachingModules;


    public void setUser(User user) {
        this.user = user;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Module> getTeachingModules() {
        return teachingModules;
    }

    public void setTeachingModules(List<Module> teachingModules) {
        this.teachingModules = teachingModules;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Department getDepartment() {
        return department;
    }


    
}
