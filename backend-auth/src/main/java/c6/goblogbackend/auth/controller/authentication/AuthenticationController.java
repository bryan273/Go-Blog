package c6.goblogbackend.auth.controller.authentication;

import c6.goblogbackend.auth.dto.authentication.AuthenticationResponse;
import c6.goblogbackend.auth.dto.authentication.LoginRequest;
import c6.goblogbackend.auth.dto.authentication.LoginResponse;
import c6.goblogbackend.auth.dto.authentication.RegisterRequest;
import c6.goblogbackend.auth.dto.authentication.RegisterResponse;
import c6.goblogbackend.auth.service.proxy.AuthenticationProxyImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {"*"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationProxyImpl authenticationProxyImpl;

    @Operation(summary = "Register new account")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register (
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationProxyImpl.handleRegister(request));
    }

    @Operation(summary = "Log in to the account")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authenticationProxyImpl.handleLogin(request));
    }

    @Operation(summary = "Authenticate the account")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login (
            @Parameter(description = "API key") @RequestHeader("Authorization") String apiKey, @RequestHeader("X-JWT-TOKEN") String jwt
    ) {
        return ResponseEntity.ok(authenticationProxyImpl.handleAuthenticate(apiKey, jwt));
    }

}

