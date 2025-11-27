package org.lms.Entity;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;


    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }


}
