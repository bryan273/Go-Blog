package c6.goblogbackend.service.authentication;

import c6.goblogbackend.auth.dto.authentication.*;
import c6.goblogbackend.auth.exception.*;
import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.repository.UserRepository;
import c6.goblogbackend.auth.service.JWTService;
import c6.goblogbackend.auth.service.authentication.AuthenticationServiceImpl;
import c6.goblogbackend.auth.service.proxy.AuthenticationProxyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationProxyImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationServiceImpl authenticationServiceImpl;

    @Mock
    private JWTService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AuthenticationProxyImpl authenticationProxyImplMock;

    @InjectMocks
    private AuthenticationProxyImpl authenticationProxyImpl;

    private AuthenticationProxyImpl authenticationProxyImplReal;

    @Value("${api-key}")
    private String APIKEY;

    @BeforeEach
    void setUp() {
        authenticationProxyImplReal = new AuthenticationProxyImpl(authenticationManager, userRepository, authenticationServiceImpl, jwtService);
        authenticationProxyImplReal.setApiKey("apikey12345"); // Set the apiKey field with a non-null value
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleRegister() {
        // Mocking the request
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("johndoe")
                .fullname("John Doe")
                .email("john@example.com")
                .gender("MALE")
                .password("password123")
                .birthdate("1990-01-01")
                .build();

        // Mocking the response
        RegisterResponse expectedResponse = RegisterResponse.builder()
                .isSuccessful(true)
                .username("johndoe")
                .build();

        // Mocking the userRepository.findByUsername() method
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());

        // Mocking the userRepository.findByEmail() method
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());

        // Mocking the authenticationService.register() method
        when(authenticationServiceImpl.register(registerRequest)).thenReturn(expectedResponse);

        // Call the method under test
        RegisterResponse response = authenticationProxyImpl.handleRegister(registerRequest);

        // Verify the interactions
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
        verify(authenticationServiceImpl, times(1)).register(registerRequest);

        // Verify the response
        assertEquals(expectedResponse, response);
    }

    @Test
    void testHandleRegister_usernameAlreadyExists() {
        // Mocking the request
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("johndoe")
                .fullname("John Doe")
                .email("john@example.com")
                .gender("MALE")
                .password("password123")
                .birthdate("1990-01-01")
                .build();

        // Mocking the userRepository.findByUsername() method
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(new User()));

        // Mocking the userRepository.findByEmail() method
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());

        // Call the method under test and verify the exception
        assertThrows(UsernameAlreadyExistException.class, () -> authenticationProxyImpl.handleRegister(registerRequest));

        // Verify the interactions
        verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(authenticationServiceImpl, never()).register(registerRequest);
    }

    @Test
    void testHandleRegister_emailAlreadyExists() {
        // Mocking the request
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("johndoe")
                .fullname("John Doe")
                .email("john@example.com")
                .gender("MALE")
                .password("password123")
                .birthdate("1990-01-01")
                .build();

        // Mocking the userRepository.findByUsername() method
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());

        // Mocking the userRepository.findByEmail() method
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Call the method under test and verify the exception
        assertThrows(EmailAlreadyExistException.class, () -> authenticationProxyImpl.handleRegister(registerRequest));

        // Verify the interactions
        verify(userRepository, never()).findByUsername(registerRequest.getUsername());
        verify(userRepository, times(1)).findByEmail(registerRequest.getEmail());
        verify(authenticationServiceImpl, never()).register(registerRequest);
    }


    @Test
    void testHandleLogin() {
        // Mocking the request
        LoginRequest loginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("password123")
                .build();

        // Mocking the user
        User user = User.builder()
                .username("johndoe")
                .build();

        // Mocking the response
        LoginResponse expectedResponse = LoginResponse.builder()
                .token("jwtToken")
                .username("johndoe")
                .userId(1)
                .build();

        // Mocking the userRepository.findByEmail() method
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(java.util.Optional.of(user));

        // Mocking the authenticationService.login() method
        when(authenticationServiceImpl.login(user)).thenReturn(expectedResponse);

        // Call the method under test
        LoginResponse response = authenticationProxyImpl.handleLogin(loginRequest);

        // Verify the interactions
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(authenticationServiceImpl, times(1)).login(user);

        // Verify the response
        assertEquals(expectedResponse, response);
    }

    @Test
    void testHandleAuthenticate() throws InvalidApiKeyException, InvalidJWTException, ExpiredJWTException {
        // Mocking the JWT token
        String jwtToken = "jwtToken";
        APIKEY = "go-blogapikey12345";

        // Mocking the request.getHeader() method
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwtToken);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + APIKEY);

        // Mocking the jwtService.verify() method
        when(jwtService.verify(jwtToken)).thenReturn(true);

        // Mocking the jwtService.isTokenExpired() method
        when(jwtService.isTokenExpired(jwtToken)).thenReturn(false);

        // Mocking the jwtService.extractSubject() method
        when(jwtService.extractSubject(jwtToken)).thenReturn("john@example.com");

        // Mocking the userRepository.findByEmail() method
        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(new User()));

        // Mocking the authenticationService.authenticate() method
        when(authenticationServiceImpl.authenticate("")).thenReturn(AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("johndoe")
                .build());

        doNothing().when(authenticationProxyImplMock).checkApiKey(APIKEY);

        // Call the method under test
        AuthenticationResponse response = authenticationProxyImplMock.handleAuthenticate(APIKEY, jwtToken);
    }

    @Test
    void handleAuthenticate_InvalidApiKeyAndInvalidJwt() {
        // Mocking the JWT token
        String jwtToken = "jwtToken";

        // Mocking the request.getHeader() method
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwtToken);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + APIKEY);

        // Mocking the jwtService.verify() method
        when(jwtService.verify(jwtToken)).thenReturn(true);

        // Mocking the jwtService.isTokenExpired() method
        when(jwtService.isTokenExpired(jwtToken)).thenReturn(false);

        // Mocking the jwtService.extractSubject() method
        when(jwtService.extractSubject(jwtToken)).thenReturn("john@example.com");

        // Mocking the userRepository.findByEmail() method
        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(new User()));

        // Mocking the authenticationService.authenticate() method
        when(authenticationServiceImpl.authenticate("")).thenReturn(AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("johndoe")
                .build());

        // Call the method under test
        assertThrows(InvalidApiKeyException.class , () -> authenticationProxyImpl.handleAuthenticate("APIKEY 7",jwtToken));
    }

    @Test
    void handleAuthenticate_ValidApiKeyAndInvalidJwt() throws InvalidApiKeyException, InvalidJWTException, ExpiredJWTException {
        // Mocking the JWT token
        String jwtToken = "jwtToken";
        APIKEY = "go-blogapikey12345";

        // Mocking the request.getHeader() method
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwtToken);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + APIKEY);

        // Mocking the jwtService.verify() method
        when(jwtService.verify(jwtToken)).thenReturn(true);

        // Mocking the jwtService.isTokenExpired() method
        when(jwtService.isTokenExpired(jwtToken)).thenReturn(false);

        // Mocking the jwtService.extractSubject() method
        when(jwtService.extractSubject(jwtToken)).thenReturn("john@example.com");

        // Mocking the userRepository.findByEmail() method
        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(new User()));

        // Mocking the authenticationService.authenticate() method
        when(authenticationServiceImpl.authenticate("")).thenReturn(AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("johndoe")
                .build());

        // Call the method under test
        assertThrows(NullPointerException.class , () -> authenticationProxyImplReal.handleAuthenticate(APIKEY,jwtToken));
    }
}
