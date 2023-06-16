package c6.goblogbackend.service.user;

import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;
import c6.goblogbackend.auth.exception.ExpiredJWTException;
import c6.goblogbackend.auth.exception.InvalidJWTException;
import c6.goblogbackend.auth.exception.UserNotExistException;
import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.repository.UserRepository;
import c6.goblogbackend.auth.service.JWTService;
import c6.goblogbackend.auth.service.proxy.UserProxyImpl;
import c6.goblogbackend.auth.service.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProxyImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private JWTService jwtService;
    @Mock
    private HttpServletRequest request;

    private UserProxyImpl userProxyImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userProxyImpl = new UserProxyImpl(userRepository, userServiceImpl, jwtService);
    }

    @Test
    void testHandleRead_validJWT_shouldReturnUserProfileResponse() {
        // Arrange
        String jwt = "valid_jwt";
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@test.com");
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwt);
        when(jwtService.verify(jwt)).thenReturn(true);
        when(jwtService.isTokenExpired(jwt)).thenReturn(false);
        when(jwtService.extractSubject(jwt)).thenReturn("test@test.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userServiceImpl.getUserProfile(user)).thenReturn(new UserProfileResponse());

        // Act
        UserProfileResponse result = userProxyImpl.handleRead(jwt);

        // Assert
        assertNotNull(result);
        verify(jwtService).verify(jwt);
        verify(jwtService).isTokenExpired(jwt);
        verify(jwtService).extractSubject(jwt);
        verify(userRepository).findByEmail(user.getEmail());
        verify(userServiceImpl).getUserProfile(user);
    }


    @Test
    void testHandleRead_invalidJWT_shouldThrowInvalidJWTException() {
        // Arrange
        String jwt = "invalid_jwt";
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwt);
        when(jwtService.verify(jwt)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidJWTException.class, () -> userProxyImpl.handleRead(jwt));
        verify(jwtService).verify(jwt);
        verifyNoMoreInteractions(jwtService);
        verifyNoInteractions(userRepository, userServiceImpl);
    }

    @Test
    void testHandleRead_expiredJWT_shouldThrowExpiredJWTException() {
        // Arrange
        String jwt = "expired_jwt";
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwt);
        when(jwtService.verify(jwt)).thenReturn(true);
        when(jwtService.isTokenExpired(jwt)).thenReturn(true);

        // Act & Assert
        assertThrows(ExpiredJWTException.class, () -> userProxyImpl.handleRead(jwt));
        verify(jwtService).verify(jwt);
        verify(jwtService).isTokenExpired(jwt);
        verifyNoMoreInteractions(jwtService);
        verifyNoInteractions(userRepository, userServiceImpl);
    }

    @Test
    void testHandleRead_userNotFound_shouldThrowUserNotExistException() {
        // Arrange
        String jwt = "valid_jwt";
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@test.com");
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwt);
        when(jwtService.verify(jwt)).thenReturn(true);
        when(jwtService.isTokenExpired(jwt)).thenReturn(false);
        when(jwtService.extractSubject(jwt)).thenReturn("test@test.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotExistException.class, () -> userProxyImpl.handleRead(jwt));
        verify(jwtService).verify(jwt);
        verify(jwtService).isTokenExpired(jwt);
        verify(jwtService).extractSubject(jwt);
        verify(userRepository).findByEmail(user.getEmail());
        verifyNoMoreInteractions(jwtService);
        verifyNoInteractions(userServiceImpl);
    }

    @Test
    void testHandleUpdate_validJWT_shouldReturnEditProfileResponse() {
        // Arrange
        String jwt = "valid_jwt";
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@test.com");
        EditProfileRequest editProfileRequest = EditProfileRequest.builder()
                .username(user.getUsername())
                .fullname("John Doe")
                .description("Updated description")
                .build();
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwt);
        when(jwtService.verify(jwt)).thenReturn(true);
        when(jwtService.isTokenExpired(jwt)).thenReturn(false);
        when(jwtService.extractSubject(jwt)).thenReturn("test@test.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userServiceImpl.editUserProfile((editProfileRequest), (user))).thenReturn(new EditProfileResponse());

        // Act
        EditProfileResponse result = userProxyImpl.handleUpdate(jwt, editProfileRequest);

        // Assert
        assertNotNull(result);
        verify(jwtService).verify(jwt);
        verify(jwtService).isTokenExpired(jwt);
        verify(jwtService).extractSubject(jwt);
        verify(userRepository).findByEmail(user.getEmail());
        verify(userServiceImpl).editUserProfile((editProfileRequest), (user));
    }


    @Test
    void testHandleUpdate_invalidJWT_shouldThrowInvalidJWTException() {
        // Arrange
        String jwt = "invalid_jwt";
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwt);
        when(jwtService.verify(jwt)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidJWTException.class, () -> userProxyImpl.handleUpdate(jwt, null));
        verify(jwtService).verify(jwt);
        verifyNoMoreInteractions(jwtService);
        verifyNoInteractions(userRepository, userServiceImpl);
    }

    @Test
    void testHandleUpdate_expiredJWT_shouldThrowExpiredJWTException() {
        // Arrange
        String jwt = "expired_jwt";
        when(request.getHeader("X-JWT-TOKEN")).thenReturn(jwt);
        when(jwtService.verify(jwt)).thenReturn(true);
        when(jwtService.isTokenExpired(jwt)).thenReturn(true);

        // Act & Assert
        assertThrows(ExpiredJWTException.class, () -> userProxyImpl.handleUpdate(jwt, null));
        verify(jwtService).verify(jwt);
        verify(jwtService).isTokenExpired(jwt);
        verifyNoMoreInteractions(jwtService);
        verifyNoInteractions(userRepository, userServiceImpl);
    }

    @Test
    void testHandleUpdate_userNotFound_shouldThrowUserNotExistException() {
        // Arrange
        String jwt = "valid_jwt";
        User user = new User();

        EditProfileRequest request = EditProfileRequest.builder()
                .build();

        user.setUsername("testUser");
        user.setEmail("test@test.com");
        when(jwtService.verify(jwt)).thenReturn(true);
        when(jwtService.isTokenExpired(jwt)).thenReturn(false);
        when(jwtService.extractSubject(jwt)).thenReturn("test@test.com");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotExistException.class, () -> userProxyImpl.handleUpdate(jwt, request));
        verify(jwtService).verify(jwt);
        verify(jwtService).isTokenExpired(jwt);
        verify(jwtService).extractSubject(jwt);
        verify(userRepository).findByEmail(user.getEmail());
        verifyNoMoreInteractions(jwtService);
        verifyNoInteractions(userServiceImpl);
    }
}
