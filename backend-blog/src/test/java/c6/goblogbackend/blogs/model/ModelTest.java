package c6.goblogbackend.blogs.model;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelTest {

    @Test
    void testApplicationUserPermission() {
        // Arrange
        ApplicationUserPermission permission = ApplicationUserPermission.USER_READ;

        // Act
        String permissionValue = permission.getPermission();

        // Assert
        assertEquals("user:read", permissionValue);
    }

    @Test
    void testApplicationUserRole() {
        // Arrange
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

        // Act
        ApplicationUserRole userRole = ApplicationUserRole.USER;

        // Assert
        assertEquals(permissions, userRole.getPermissions());
    }

    @Test
    void testComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setCommentId(1);
        comment.setCreator(null);
        comment.setPostId(1);
        comment.setTimeCreated(null);
        comment.setContent("Comment 1");

        // Act
        Integer commentId = comment.getCommentId();
        User creator = comment.getCreator();
        Integer postId = comment.getPostId();
        Date timeCreated = comment.getTimeCreated();
        String content = comment.getContent();

        // Assert
        assertEquals(1, commentId);
        assertEquals(null, creator);
        assertEquals(1, postId);
        assertEquals(null, timeCreated);
        assertEquals("Comment 1", content);
    }

    @Test
    void testPost() {
        // Arrange
        Post post = new Post();
        post.setPostId(1L);
        post.setCreator(null);
        post.setTitle("Post 1");
        post.setContent("Content 1");
        post.setLikes(0);
        post.setTimeCreated(null);

        // Act
        Long postId = post.getPostId();
        User creator = post.getCreator();
        String title = post.getTitle();
        String content = post.getContent();
        int likes = post.getLikes();
        Date timeCreated = post.getTimeCreated();

        // Assert
        assertEquals(1L, postId);
        assertEquals(null, creator);
        assertEquals("Post 1", title);
        assertEquals("Content 1", content);
        assertEquals(0, likes);
        assertEquals(null, timeCreated);
    }

    @Test
    void testUser() {
        // Test User class
        User user = new User();
        user.setId(1);
        user.setFullname("John Doe");
        user.setPassword("password123");
        user.setBirthdate("1990-01-01");
        user.setGender(Gender.MALE);
        user.setEmail("john.doe@example.com");
        user.setUsername("john_doe");
        user.setActive(true);

        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getFullname());
        assertEquals("password123", user.getPassword());
        assertEquals("1990-01-01", user.getBirthdate());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("john_doe", user.getUsername());
        assertTrue(user.isActive());
    }
}