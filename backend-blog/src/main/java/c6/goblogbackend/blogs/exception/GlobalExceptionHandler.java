package c6.goblogbackend.blogs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {UsernameAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> userExist() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                "username has been taken",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {EmailAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> emailExist() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                "email has been used",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {AuthenticationException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> credentialsError(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        ErrorTemplate baseException = new ErrorTemplate(
                "incorrect password or username",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> generalError(Exception exception) {
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


    @ExceptionHandler(value = { InvalidJWTException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> jwtError(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        ErrorTemplate baseException = new ErrorTemplate(
                "Invalid jwt",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = { ExpiredJWTException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> expiredJWT(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        ErrorTemplate baseException = new ErrorTemplate(
                "Token has expired",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {UserNotExistException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> userNotFound(Exception exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ErrorTemplate baseException = new ErrorTemplate(
                "User not found",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }


    @ExceptionHandler(value = {InvalidApiKeyException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> invalidApiKey(Exception exception) {
        HttpStatus badRequest = HttpStatus.FORBIDDEN;
        ErrorTemplate baseException = new ErrorTemplate(
                "who are you?",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }
}
