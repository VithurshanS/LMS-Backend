package org.lms.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="lecturer")
public class Lecturer {

    @Id
    @GeneratedValue
    @Column(name = "lecturer_id")
    private UUID id;


    @Column(name = "user_id", nullable = false,unique = true)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Module> teachingModules = new ArrayList<>();

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Lecturer (){}

    public Lecturer(UUID userId, Department department) {
        this.userId = userId;
        this.department = department;
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




    public Department getDepartment() {
        return department;
    }

}
