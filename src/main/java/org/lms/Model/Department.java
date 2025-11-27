package org.lms.Model;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
