package org.lms.Model;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name="module_id")
    private Module module;

    public UUID getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Module getModule() {
        return module;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setModule(Module module) {
        this.module = module;
    }




    
    
}
