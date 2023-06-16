package c6.goblogbackend.model;

import c6.goblogbackend.auth.model.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelTest {

    @Test
    void testApplicationUserPermission() {
        ApplicationUserPermission permission = ApplicationUserPermission.USER_READ;
        assertEquals("user:read", permission.getPermission());
    }

    @Test
    void testApplicationUserRole() {
        Set<ApplicationUserPermission> permissions = new HashSet<>();
        permissions.add(ApplicationUserPermission.USER_READ);
        permissions.add(ApplicationUserPermission.USER_CREATE);
        permissions.add(ApplicationUserPermission.USER_UPDATE);
        permissions.add(ApplicationUserPermission.BLOG_READ);
        permissions.add(ApplicationUserPermission.BLOG_CREATE);
        permissions.add(ApplicationUserPermission.BLOG_UPDATE);
        permissions.add(ApplicationUserPermission.BLOG_DELETE);
        permissions.add(ApplicationUserPermission.COMMENT_READ);
        permissions.add(ApplicationUserPermission.COMMENT_CREATE);
        permissions.add(ApplicationUserPermission.COMMENT_UPDATE);
        permissions.add(ApplicationUserPermission.COMMENT_DELETE);

        ApplicationUserRole role = ApplicationUserRole.USER;
        assertEquals(permissions, role.getPermissions());
    }

    @Test
    void testGender() {
        Gender gender = Gender.MALE;
        assertEquals("MALE", gender.name());
    }

    @Test
    void testUser() {
        User user = new User();
        user.setId(1);
        user.setFullname("John Doe");
        user.setPassword("password");
        user.setBirthdate("1990-01-01");
        user.setGender(Gender.MALE);
        user.setEmail("john@example.com");
        user.setUsername("john");
        user.setActive(true);

        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getFullname());
        assertEquals("password", user.getPassword());
        assertEquals("1990-01-01", user.getBirthdate());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("john", user.getUsername());
        assertTrue(user.isActive());
        assertEquals(ApplicationUserRole.USER.getGrantedAuthority(), user.getAuthorities());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}

