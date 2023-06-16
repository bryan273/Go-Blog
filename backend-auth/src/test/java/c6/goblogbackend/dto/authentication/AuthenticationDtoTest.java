package c6.goblogbackend.dto.authentication;

import c6.goblogbackend.auth.dto.authentication.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthenticationDtoTest {

    @Test
    void testAuthenticationRequest() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .apikey("API_KEY")
                .jwt("JWT_TOKEN")
                .build();

        Assertions.assertEquals("API_KEY", request.getApikey());
        Assertions.assertEquals("JWT_TOKEN", request.getJwt());
    }

    @Test
    void testAuthenticationResponse() {
        AuthenticationResponse response = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("JohnDoe")
                .build();

        Assertions.assertTrue(response.isAuthenticated());
        Assertions.assertEquals(1, response.getUserId());
        Assertions.assertEquals("JohnDoe", response.getUsername());
    }

    @Test
    void testLoginRequest() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        Assertions.assertEquals("test@example.com", request.getEmail());
        Assertions.assertEquals("password123", request.getPassword());
    }

    @Test
    void testLoginResponse() {
        LoginResponse response = LoginResponse.builder()
                .token("TOKEN_STRING")
                .userId(2)
                .username("JaneDoe")
                .build();

        Assertions.assertEquals("TOKEN_STRING", response.getToken());
        Assertions.assertEquals(2, response.getUserId());
        Assertions.assertEquals("JaneDoe", response.getUsername());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request = RegisterRequest.builder()
                .username("johndoe")
                .fullname("John Doe")
                .email("johndoe@example.com")
                .gender("Male")
                .password("password123")
                .birthdate("1990-01-01")
                .build();

        Assertions.assertEquals("johndoe", request.getUsername());
        Assertions.assertEquals("John Doe", request.getFullname());
        Assertions.assertEquals("johndoe@example.com", request.getEmail());
        Assertions.assertEquals("Male", request.getGender());
        Assertions.assertEquals("password123", request.getPassword());
        Assertions.assertEquals("1990-01-01", request.getBirthdate());
    }

    @Test
    void testRegisterResponse() {
        RegisterResponse response = RegisterResponse.builder()
                .username("johndoe")
                .isSuccessful(true)
                .build();

        Assertions.assertEquals("johndoe", response.getUsername());
        Assertions.assertTrue(response.isSuccessful());
    }
}
