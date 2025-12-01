package org.lms.EntityRelationshipTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.lms.Model.UserDB;
import org.lms.Model.UserRole;
import org.hibernate.PropertyValueException;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@QuarkusTest
public class UserDBTest {
    @Inject
    EntityManager em;

    @Test
    @Transactional
    public void testUserCreation() {
        UserDB userDB = new UserDB("John", "Doe", "user1@gmail.com", "admin", "user11", "password");
        em.persist(userDB);
        em.flush();
        
        assertNotNull(userDB.getId());
        assertEquals("John", userDB.getFirstName());
        assertEquals("Doe", userDB.getLastName());
        assertEquals("John Doe", userDB.getFullName());
        assertEquals("user1@gmail.com", userDB.getEmail());
        assertEquals(UserRole.ADMIN, userDB.getRole());
        assertEquals("user11", userDB.getUsername());
        assertEquals("password", userDB.getPassword());
        assertTrue(userDB.isActive());
        assertFalse(userDB.isApproved());
    }

    @Test
    @Transactional
    public void testEmailCannotBeNull() {
        PropertyValueException exception = assertThrows(PropertyValueException.class, () -> {
            UserDB userDB = new UserDB("Test", "User", null, "student", "testuseremailnull", "password123");
            em.persist(userDB);
        }, "Email should not be null");
        assertNotNull(exception);
    }



    @Test
    @Transactional
    public void testUsernameCannotBeNull() {
        PropertyValueException exception = assertThrows(PropertyValueException.class, () -> {
            UserDB userDB = new UserDB("Test", "User", "testusername@example.com", "student", null, "password123");
            em.persist(userDB);
        }, "Username should not be null");
        assertNotNull(exception);
    }

    @Test
    @Disabled
    @Transactional
    public void testPasswordCannotBeNull() {
        PropertyValueException exception = assertThrows(PropertyValueException.class, () -> {
            UserDB userDB = new UserDB("Test", "User", "testpassword@example.com", "student", "testuserpassnull", null);
            em.persist(userDB);
        }, "Password should not be null");
        assertNotNull(exception);
    }

    @Test
    @Transactional
    public void testFirstNameCanNotBeNull() {
        PropertyValueException exception = assertThrows(PropertyValueException.class, () -> {
            UserDB userDB = new UserDB(null, "User", "testnamenull@example.com", "student", "testusenamenull", "password123");
            em.persist(userDB);

        }, "First name cannot be null");
        assertNotNull(exception);

    }

    @Test
    @Transactional
    public void testLastNameCanNotBeNull() {
        PropertyValueException exception = assertThrows(PropertyValueException.class, () -> {
            UserDB userDB = new UserDB("Test", null, "testlastnamenull@example.com", "student", "testuserlastnamenull", "password123");
            em.persist(userDB);

        }, "Last name cannot be null");
        assertNotNull(exception);

    }

    @Test
    @Transactional
    public void testUsernameUniqueness()  {
        UserDB userDB1 = new UserDB("User", "One", "user1unique@example.com", "student", "uniqueusername123", "password123");
        em.persist(userDB1);
        em.flush();
        UserDB userDB2 = new UserDB("User", "Two", "user2unique@example.com", "lecturer", "uniqueusername123", "password456");
        em.persist(userDB2);
        
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            em.flush();
        }, "Username should be unique");
        assertNotNull(exception);
    }

}
