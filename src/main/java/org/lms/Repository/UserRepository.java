package org.lms.Repository;

import java.util.UUID;

import org.lms.Model.UserDB;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserDB, UUID> {
}
