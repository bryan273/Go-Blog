package c6.goblogbackend.controller;

import c6.goblogbackend.auth.controller.user.UserController;
import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;
import c6.goblogbackend.auth.service.proxy.UserProxyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserProxyImpl userProxyImpl;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserProfile() {

        String jwt = "JWT";
        // Mocking the response from UserProxy
        UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                .username("john doe")
                .fullname("John Doe")
                .email("john@example.com")
                .gender("Male")
                .birthdate("1990-01-01")
                .build();
        when(userProxyImpl.handleRead(jwt)).thenReturn(userProfileResponse);

        // Call the method under test
        ResponseEntity<UserProfileResponse> responseEntity = userController.getUserProfile(jwt);

        // Verify the interactions
        verify(userProxyImpl, times(1)).handleRead(jwt);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userProfileResponse, responseEntity.getBody());
    }

    @Test
    void testEditUserProfile() {

        String jwt = "JWT";
        // Mocking the response from UserProxy
        EditProfileResponse editProfileResponse = EditProfileResponse.builder()
                .username("johndoe")
                .fullname("John Doe")
                .email("john@example.com")
                .build();
        EditProfileRequest editProfileRequest = EditProfileRequest.builder()
                .username("johndoe")
                .fullname("John Doe")
                .build();

        when(userProxyImpl.handleUpdate(jwt, editProfileRequest)).thenReturn(editProfileResponse);

        // Call the method under test
        ResponseEntity<EditProfileResponse> responseEntity = userController.editUserProfile(jwt, editProfileRequest);

        // Verify the interactions
        verify(userProxyImpl, times(1)).handleUpdate(jwt, editProfileRequest);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(editProfileResponse, responseEntity.getBody());
    }
}
