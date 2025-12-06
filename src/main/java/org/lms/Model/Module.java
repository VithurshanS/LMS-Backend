package org.lms.Model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="module")
public class Module {

    @Id
    @GeneratedValue
    @Column(name="module_id")
    private UUID id;

    @Column(unique=true,name="modulecode",columnDefinition = "varchar(7)",nullable=false)
    private String module_code;

    private String name;

    private int enrollmentLimit;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="department_id")
    private Department department;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="admin_id")
    private Admin createdby;

    @OneToMany(mappedBy="module")
    @JsonIgnore
    private List<Enrollment> moduleEnrollments;

    


    public Module (){}


    public Module(String module_code, String name, int enrollmentLimit, Department department, Lecturer lecturer, Admin createdby) {
        this.module_code = module_code;
        this.name = name;
        this.enrollmentLimit = enrollmentLimit;
        this.department = department;
        this.lecturer = lecturer;
        this.createdby = createdby;
    }

    public List<Enrollment> getModuleEnrollments() {
        return moduleEnrollments;
    }

    public void setModuleEnrollments(List<Enrollment> moduleEnrollments) {
        this.moduleEnrollments = moduleEnrollments;
    }

    public Admin getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Admin createdby) {
        this.createdby = createdby;
    }

    public UUID getId() {
        return id;
    }

    public String getModule_code() {
        return module_code;
    }

    public String getName() {
        return name;
    }

    public int getLimit() {
        return enrollmentLimit;
    }

    public Department getDepartment() {
        return department;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setModule_code(String module_code) {
        this.module_code = module_code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLimit(int limit) {
        this.enrollmentLimit = limit;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    

    
    
}
