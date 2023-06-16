package c6.goblogbackend.service.authentication;

import c6.goblogbackend.auth.dto.authentication.AuthenticationResponse;
import c6.goblogbackend.auth.dto.authentication.LoginResponse;
import c6.goblogbackend.auth.dto.authentication.RegisterRequest;
import c6.goblogbackend.auth.dto.authentication.RegisterResponse;
import c6.goblogbackend.auth.exception.InvalidApiKeyException;
import c6.goblogbackend.auth.exception.InvalidJWTException;
import c6.goblogbackend.auth.exception.UserNotExistException;
import c6.goblogbackend.auth.model.Gender;
import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.repository.UserRepository;
import c6.goblogbackend.auth.service.JWTService;
import c6.goblogbackend.auth.service.authentication.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Mocking the request
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("johndoe");
        registerRequest.setFullname("John Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setGender("MALE");
        registerRequest.setPassword("password123");
        registerRequest.setBirthdate("1990-01-01");

        // Mocking the user
        User user = User.builder()
                .active(true)
                .username("johndoe")
                .fullname("John Doe")
                .email("john@example.com")
                .gender(Gender.MALE)
                .password("encodedPassword")
                .birthdate("1990-01-01")
                .build();

        // Mocking the response
        RegisterResponse expectedResponse = RegisterResponse.builder()
                .isSuccessful(true)
                .username("johndoe")
                .build();

        // Mocking the userRepository.save() method
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the method under test
        RegisterResponse response = authenticationServiceImpl.register(registerRequest);

        // Verify the interactions
        verify(userRepository, times(1)).save(any(User.class));

        // Verify the response
        assertEquals(expectedResponse, response);
    }

    @Test
    void testLogin() {
        // Mocking the user
        User user = User.builder()
                .id(1)
                .username("johndoe")
                .build();

        // Mocking the JWT token
        String jwtToken = "jwtToken";

        // Mocking the response
        LoginResponse expectedResponse = LoginResponse.builder()
                .token("jwtToken")
                .username("johndoe")
                .userId(1)
                .build();

        // Mocking the jwtService.generateToken() method
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        // Call the method under test
        LoginResponse response = authenticationServiceImpl.login(user);

        // Verify the interactions
        verify(jwtService, times(1)).generateToken(user);

        // Verify the response
        assertEquals(expectedResponse, response);
    }

    @Test
    void testAuthenticate() throws UserNotExistException, InvalidApiKeyException, InvalidJWTException {
        // Mocking the user
        User user = User.builder()
                .id(1)
                .username("johndoe")
                .email("john@john.com")
                .build();
        when(userRepository.findByEmail("john@john.com")).thenReturn(java.util.Optional.of(user));

        // Mocking the response
        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(1)
                .username("johndoe")
                .build();

        // Call the method under test
        AuthenticationResponse response = authenticationServiceImpl.authenticate("john@john.com");

        // Verify the response
        assertEquals(expectedResponse, response);
    }
}
