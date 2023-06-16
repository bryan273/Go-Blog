package c6.goblogbackend.auth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

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
        var baseException = new ErrorTemplate(
                "username has been taken",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {EmailAlreadyExistException.class})
    public ResponseEntity<Object> emailExist() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        var baseException = new ErrorTemplate(
                "email has been used",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {AuthenticationException.class, UsernameNotFoundException.class})
    public ResponseEntity<Object> credentialsError(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        var baseException = new ErrorTemplate(
                "incorrect password or username",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> generalError(Exception exception) {
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        var baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {MalformedJwtException.class, UnsupportedJwtException.class, SignatureException.class, InvalidJWTException.class})
    public ResponseEntity<Object> invalidJWT(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        var baseException = new ErrorTemplate(
                "Invalid jwt detected. make sure the format, alg header and sign is correct",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {ExpiredJWTException.class, ExpiredJwtException.class})
    public ResponseEntity<Object> expiredJWT(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        var baseException = new ErrorTemplate(
                "The token provided has expired. Get the new by doing login again.",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {UserNotExistException.class})
    public ResponseEntity<Object> userNotFound(Exception exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        var baseException = new ErrorTemplate(
                "User not found",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {InvalidApiKeyException.class})
    public ResponseEntity<Object> invalidApiKey(Exception exception) {
        HttpStatus badRequest = HttpStatus.FORBIDDEN;
        var baseException = new ErrorTemplate(
                "who are you?",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(baseException, badRequest);
    }
}
