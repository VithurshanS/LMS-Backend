package org.lms.Model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import org.lms.User.User;

@Entity
@Table(name = "admin")
public class  Admin {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToMany(mappedBy="createdby")
    private List<Module> createdModules;

    public Admin() {}

    public Admin(UUID userId) {
        this.userId = userId;
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

    public UUID getUserId() {
        return userId;
    }

}
