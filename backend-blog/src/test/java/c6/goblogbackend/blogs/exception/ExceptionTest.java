package c6.goblogbackend.blogs.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.naming.AuthenticationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionTest {
    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testCommentDoesNotExistException() {
        Integer commentId = 1;
        CommentDoesNotExistException exception = new CommentDoesNotExistException(commentId);

        assertEquals("Comment with id " + commentId + " is empty", exception.getMessage());
    }

    @Test
    void testEmailAlreadyExistException() {
        EmailAlreadyExistException exception = new EmailAlreadyExistException();

        Assertions.assertNull(exception.getMessage());
    }

    @Test
    void testErrorTemplate() {
        String message = "Test error";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Z"));

        ErrorTemplate errorTemplate = new ErrorTemplate(message, httpStatus, timestamp);

        assertEquals(message, errorTemplate.message());
        assertEquals(httpStatus, errorTemplate.httpStatus());
        assertEquals(timestamp, errorTemplate.timestamp());
    }

    @Test
    void testExpiredJWTException() {
        ExpiredJWTException exception = new ExpiredJWTException();

        Assertions.assertNull(exception.getMessage());
    }

    @Test
    void testGlobalExceptionHandler() {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        Assertions.assertNotNull(globalExceptionHandler);
    }

    @Test
    void testInvalidApiKeyException() {
        InvalidApiKeyException exception = new InvalidApiKeyException();

        Assertions.assertNull(exception.getMessage());
    }

    @Test
    void testInvalidJWTException() {
        InvalidJWTException exception = new InvalidJWTException();

        Assertions.assertNull(exception.getMessage());
    }

    @Test
    void testPostDoesNotExistException() {
        Integer postId = 1;
        PostDoesNotExistException exception = new PostDoesNotExistException(postId);

        assertEquals("Post with id " + postId + " does not exist", exception.getMessage());
    }

    @Test
    void testUsernameAlreadyExistException() {
        UsernameAlreadyExistException exception = new UsernameAlreadyExistException();

        Assertions.assertNull(exception.getMessage());
    }

    @Test
    void testUserNotExistException() {
        UserNotExistException exception = new UserNotExistException();

        Assertions.assertNull(exception.getMessage());
    }

    @Test
    void testGlobalUserExist() {
        // Act
        ResponseEntity<Object> response = globalExceptionHandler.userExist();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("username has been taken", errorTemplate.message());
        assertEquals(HttpStatus.BAD_REQUEST, errorTemplate.httpStatus());
    }

    @Test
    void testGlobalEmailExist() {
        // Act
        ResponseEntity<Object> response = globalExceptionHandler.emailExist();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("email has been used", errorTemplate.message());
        assertEquals(HttpStatus.BAD_REQUEST, errorTemplate.httpStatus());
    }

    @Test
    void testGlobalGeneralError() {
        // Arrange
        Exception exception = new Exception("Internal server error");

        // Act
        ResponseEntity<Object> response = globalExceptionHandler.generalError(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("Internal server error", errorTemplate.message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorTemplate.httpStatus());
    }

    @Test
    void testGlobalJWTError() {
        // Arrange
        Exception exception = new InvalidJWTException();

        // Act
        ResponseEntity<Object> response = globalExceptionHandler.jwtError(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("Invalid jwt", errorTemplate.message());
        assertEquals(HttpStatus.UNAUTHORIZED, errorTemplate.httpStatus());
    }

    @Test
    void testGlobalExpiredJWT() {
        // Arrange
        Exception exception = new ExpiredJWTException();

        // Act
        ResponseEntity<Object> response = globalExceptionHandler.expiredJWT(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("Token has expired", errorTemplate.message());
        assertEquals(HttpStatus.UNAUTHORIZED, errorTemplate.httpStatus());
    }

    @Test
    void testCredentialsError() {
        // Arrange
        Exception exception = new AuthenticationException("Invalid credentials");

        // Act
        ResponseEntity<Object> response = globalExceptionHandler.credentialsError(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("incorrect password or username", errorTemplate.message());
        assertEquals(HttpStatus.UNAUTHORIZED, errorTemplate.httpStatus());
    }

    @Test
    void testGlobalUserNotFound() {
        // Arrange
        Exception exception = new UserNotExistException();

        // Act
        ResponseEntity<Object> response = globalExceptionHandler.userNotFound(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("User not found", errorTemplate.message());
        assertEquals(HttpStatus.NOT_FOUND, errorTemplate.httpStatus());
    }

    @Test
    void testGlobalInvalidApiKey() {
        // Arrange
        Exception exception = new InvalidApiKeyException();

        // Act
        ResponseEntity<Object> response = globalExceptionHandler.invalidApiKey(exception);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assertEquals("who are you?", errorTemplate.message());
        assertEquals(HttpStatus.FORBIDDEN, errorTemplate.httpStatus());
    }

    @Test
    void testPostCreationException() {
        // Arrange
        String message = "Error occurred during post creation";
        Throwable cause = new IllegalArgumentException("Invalid input");

        // Act
        PostCreationException exception = new PostCreationException(message, cause);

        // Assert
        Assertions.assertEquals(message, exception.getMessage());
        Assertions.assertEquals(cause, exception.getCause());
    }
}
