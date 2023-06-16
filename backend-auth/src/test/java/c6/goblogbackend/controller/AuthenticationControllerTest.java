package c6.goblogbackend.controller;

import c6.goblogbackend.auth.controller.authentication.AuthenticationController;
import c6.goblogbackend.auth.dto.authentication.*;
import c6.goblogbackend.auth.service.proxy.AuthenticationProxyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class AuthenticationControllerTest {

    @Mock
    private AuthenticationProxyImpl authenticationProxyImpl;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        RegisterResponse expectedResponse = new RegisterResponse();
        when(authenticationProxyImpl.handleRegister(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<RegisterResponse> responseEntity = authenticationController.register(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(authenticationProxyImpl, times(1)).handleRegister(request);
        verifyNoMoreInteractions(authenticationProxyImpl);
    }

    @Test
    void testLogin() {
        // Arrange
        LoginRequest request = new LoginRequest();
        LoginResponse expectedResponse = new LoginResponse();
        when(authenticationProxyImpl.handleLogin(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<LoginResponse> responseEntity = authenticationController.login(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(expectedResponse);
        System.out.println(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(authenticationProxyImpl, times(1)).handleLogin(request);
        verifyNoMoreInteractions(authenticationProxyImpl);
    }

    @Test
    void testAuthenticate() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setApikey("your-api-key");
        request.setJwt("your-jwt-token");

        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        when(authenticationProxyImpl.handleAuthenticate(request.getApikey(), request.getJwt())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.login(request.getApikey(), request.getJwt());

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(authenticationProxyImpl, times(1)).handleAuthenticate(request.getApikey(), request.getJwt());
        verifyNoMoreInteractions(authenticationProxyImpl);

    }

    @Test
    void testAuthenticationRequestArgs() {
        // Arrange
        String apiKey = "your-api-key";
        String jwt = "your-jwt-token";

        // Act
        AuthenticationRequest request = new AuthenticationRequest(apiKey, jwt);

        // Assert
        assertEquals(apiKey, request.getApikey());
        assertEquals(jwt, request.getJwt());
    }
}
