package org.lms.Model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue
    @Column(name = "department_id")
    private UUID id;

    @Column(nullable=false)
    private String name;

    @OneToMany(mappedBy="department",cascade=CascadeType.ALL)
    @JsonIgnore
    private List<Lecturer> lectureList;

    @OneToMany(mappedBy="department",cascade=CascadeType.ALL)
    @JsonIgnore
    private List<Student> studentList;

    @OneToMany(mappedBy = "department", cascade=CascadeType.ALL)
    @JsonIgnore
    private  List<Module> moduleList;

    public List<Module> getModuleList() {
        return moduleList;
    }

    public List<Lecturer> getLectureList() {
        return lectureList;
    }

    public List<Student> getStudentList(){
        return this.studentList;
    }

    public List<Lecturer> getLecturerList(){
        return this.lectureList;
    }

    public Department() {}

    public Department(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
