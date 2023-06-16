package c6.goblogbackend.auth.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import c6.goblogbackend.auth.dto.user.EditProfileRequest;
import c6.goblogbackend.auth.dto.user.EditProfileResponse;
import c6.goblogbackend.auth.dto.user.UserProfileResponse;
import c6.goblogbackend.auth.service.proxy.UserProxyImpl;
import org.springframework.web.bind.annotation.RequestMethod;
import lombok.RequiredArgsConstructor;



@CrossOrigin(origins = {"*"}, allowedHeaders = "*", methods = {RequestMethod.GET,RequestMethod.PUT, RequestMethod.OPTIONS})
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserProxyImpl userProxyImpl;

    @Operation(summary = "Get user profile")
    @GetMapping("/get_profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(
        @RequestHeader("X-JWT-TOKEN") String jwt
    ){
        return ResponseEntity.ok(userProxyImpl.handleRead(jwt));
    }

    @Operation(summary = "Edit user profile")
    @PutMapping("/edit_profile")
    public ResponseEntity<EditProfileResponse> editUserProfile(
        @RequestHeader("X-JWT-TOKEN") String jwt, @RequestBody EditProfileRequest request
    ){
        return ResponseEntity.ok(userProxyImpl.handleUpdate(jwt, request));
    }
    
}
