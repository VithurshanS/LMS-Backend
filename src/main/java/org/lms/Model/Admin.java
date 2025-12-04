package org.lms.Model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
@Table(name = "admin")
public class  Admin {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private UUID id;

    @Column(name = "user_id", nullable = false,unique = true)
    private UUID userId;

    @OneToMany(mappedBy="createdby")
    @JsonIgnore
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
