package org.lms.Model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private UUID id;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy="createdby")
    private List<Module> createdModules;


    public void setUser(User user) {
        this.user = user;
    }

    public List<Module> getCreatedModules() {
        return createdModules;
    }

    public void setCreatedModules(List<Module> createdModules) {
        this.createdModules = createdModules;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }


}
