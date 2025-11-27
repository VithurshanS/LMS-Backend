package org.lms.Model;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue
    @Column(name = "department_id")
    private UUID id;

    private String name;

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
