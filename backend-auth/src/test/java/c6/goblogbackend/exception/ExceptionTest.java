package c6.goblogbackend.exception;

import c6.goblogbackend.auth.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testEmailAlreadyExistException() {
        // Arrange
        EmailAlreadyExistException exception = new EmailAlreadyExistException();

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testUsernameAlreadyExistException() {
        // Arrange
        UsernameAlreadyExistException exception = new UsernameAlreadyExistException();

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testUserNotExistException() {
        // Arrange
        UserNotExistException exception = new UserNotExistException();

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testInvalidJWTException() {
        // Arrange
        InvalidJWTException exception = new InvalidJWTException();

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExpiredJWTException() {
        // Arrange
        ExpiredJWTException exception = new ExpiredJWTException();

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testInvalidApiKeyException() {
        // Arrange
        InvalidApiKeyException exception = new InvalidApiKeyException();

        // Assert
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testErrorTemplate() {
        // Arrange
        String expectedMessage = "Sample error message";
        HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        ZonedDateTime expectedTimestamp = ZonedDateTime.now(ZoneId.of("Z"));

        // Act
        ErrorTemplate errorTemplate = new ErrorTemplate(expectedMessage, expectedHttpStatus, expectedTimestamp);

        // Assert
        assertEquals(expectedMessage, errorTemplate.message());
        assertEquals(expectedHttpStatus, errorTemplate.httpStatus());
        assertEquals(expectedTimestamp, errorTemplate.timestamp());
    }

    @Test
    void testGlobalUserExistException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.userExist();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("username has been taken", errorTemplate.message());
        assertEquals(HttpStatus.BAD_REQUEST, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }

    @Test
    void testGlobalEmailExistException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.emailExist();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("email has been used", errorTemplate.message());
        assertEquals(HttpStatus.BAD_REQUEST, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }

    @Test
    void testGlobalCredentialsErrorException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.credentialsError(new Exception());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("incorrect password or username", errorTemplate.message());
        assertEquals(HttpStatus.UNAUTHORIZED, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }

    @Test
    void testGlobalGeneralErrorException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.generalError(new Exception("Some error"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("Some error", errorTemplate.message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }

    @Test
    void testGlobalInvalidJWTException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.invalidJWT(new Exception());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("Invalid jwt detected. make sure the format, alg header and sign is correct", errorTemplate.message());
        assertEquals(HttpStatus.UNAUTHORIZED, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }

    @Test
    void testGlobalExpiredJWTException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.expiredJWT(new Exception());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("The token provided has expired. Get the new by doing login again.", errorTemplate.message());
        assertEquals(HttpStatus.UNAUTHORIZED, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }

    @Test
    void testGlobalUserNotExistException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.userNotFound(new Exception());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("User not found", errorTemplate.message());
        assertEquals(HttpStatus.NOT_FOUND, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }

    @Test
    void testGlobalInvalidApiKeyException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<Object> response = handler.invalidApiKey(new Exception());

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        ErrorTemplate errorTemplate = (ErrorTemplate) response.getBody();
        assert errorTemplate != null;
        assertEquals("who are you?", errorTemplate.message());
        assertEquals(HttpStatus.FORBIDDEN, errorTemplate.httpStatus());
        assertTrue(Duration.between(ZonedDateTime.now(ZoneId.of("Z")), errorTemplate.timestamp()).toMillis() < 500);
    }


}
