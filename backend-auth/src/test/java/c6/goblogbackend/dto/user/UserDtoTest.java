package c6.goblogbackend.dto.user;

import c6.goblogbackend.auth.dto.user.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserDtoTest {
    @Test
    void testEditProfileRequest() {
        EditProfileRequest request = EditProfileRequest.builder()
                .username("johndoe")
                .fullname("John Doe")
                .description("This is my profile")
                .build();

        Assertions.assertEquals("johndoe", request.getUsername());
        Assertions.assertEquals("John Doe", request.getFullname());
        Assertions.assertEquals("This is my profile", request.getDescription());
    }

    @Test
    void testEditProfileResponse() {
        EditProfileResponse response = EditProfileResponse.builder()
                .username("johndoe")
                .fullname("John Doe")
                .email("johndoe@example.com")
                .build();

        Assertions.assertEquals("johndoe", response.getUsername());
        Assertions.assertEquals("John Doe", response.getFullname());
        Assertions.assertEquals("johndoe@example.com", response.getEmail());
    }

    @Test
    void testUserProfileResponse() {
        UserProfileResponse response = UserProfileResponse.builder()
                .username("johndoe")
                .fullname("John Doe")
                .email("johndoe@example.com")
                .gender("Male")
                .birthdate("1990-01-01")
                .build();

        Assertions.assertEquals("johndoe", response.getUsername());
        Assertions.assertEquals("John Doe", response.getFullname());
        Assertions.assertEquals("johndoe@example.com", response.getEmail());
        Assertions.assertEquals("Male", response.getGender());
        Assertions.assertEquals("1990-01-01", response.getBirthdate());
    }
}
