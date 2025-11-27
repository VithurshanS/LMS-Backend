package org.lms.Model;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name="module")
public class Module {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;

    @Column(unique=true,name="modulecode")
    private String module_code;

    private String name;

    private int limit;

    @ManyToOne
    @JoinColumn(name="department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name="lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name="admin_id")
    private Admin createdby;



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
        return limit;
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
        this.limit = limit;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    

    
    
}
