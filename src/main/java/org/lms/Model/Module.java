package org.lms.Model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name="module")
public class Module {

    @Id
    @GeneratedValue
    @Column(name="module_id")
    private UUID id;

    @Column(unique=true,name="modulecode",columnDefinition = "varchar(7)")
    private String module_code;

    private String name;

    private int enrollmentLimit;

    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name="lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin createdby;

    @OneToMany(mappedBy="module")
    private List<Enrollment> moduleEnrollments;





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
