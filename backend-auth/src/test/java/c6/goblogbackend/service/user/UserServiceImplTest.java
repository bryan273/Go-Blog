package c6.goblogbackend.service.user;

import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;
import c6.goblogbackend.auth.model.Gender;
import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.repository.UserRepository;
import c6.goblogbackend.auth.service.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userServiceImpl = new UserServiceImpl(userRepository);
    }

    @Test
    void getUserProfile_ValidUser_ReturnsUserProfileResponse() {
        // Arrange
        User user = new User();
        user.setFullname("John Doe");
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setBirthdate("1990-1-1");
        user.setGender(Gender.MALE);

        // Act
        UserProfileResponse userProfile = userServiceImpl.getUserProfile(user);

        // Assert
        Assertions.assertEquals("John Doe", userProfile.getFullname());
        Assertions.assertEquals("johndoe", userProfile.getUsername());
        Assertions.assertEquals("johndoe@example.com", userProfile.getEmail());
        Assertions.assertEquals("1990-1-1", userProfile.getBirthdate());
        Assertions.assertEquals("MALE", userProfile.getGender());
    }

    @Test
    void editUserProfile_ValidRequest_ReturnsEditProfileResponse() {
        // Arrange
        EditProfileRequest requestBody = new EditProfileRequest();
        requestBody.setFullname("Jane Smith");
        requestBody.setUsername("janesmith");

        User user = new User();
        user.setId(1);
        user.setEmail("johndoe@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        EditProfileResponse editProfileResponse = userServiceImpl.editUserProfile(requestBody, user);

        // Assert
        Assertions.assertEquals("Jane Smith", editProfileResponse.getFullname());
        Assertions.assertEquals("janesmith", editProfileResponse.getUsername());
        Assertions.assertEquals("johndoe@example.com", editProfileResponse.getEmail());
    }
}
