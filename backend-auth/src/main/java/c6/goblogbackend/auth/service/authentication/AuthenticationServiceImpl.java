package c6.goblogbackend.auth.service.authentication;


import c6.goblogbackend.auth.dto.authentication.AuthenticationResponse;
import c6.goblogbackend.auth.dto.authentication.LoginResponse;
import c6.goblogbackend.auth.dto.authentication.RegisterRequest;
import c6.goblogbackend.auth.dto.authentication.RegisterResponse;
import c6.goblogbackend.auth.exception.UserNotExistException;
import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.model.Gender;
import c6.goblogbackend.auth.repository.UserRepository;
import c6.goblogbackend.auth.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        var user = User.builder()
                .active(true)
                .username(request.getUsername())
                .fullname(request.getFullname())
                .email(request.getEmail())
                .gender(Gender.valueOf(request.getGender().toUpperCase()))
                .password(passwordEncoder.encode(request.getPassword()))
                .birthdate(request.getBirthdate())
                .build();

        userRepository.save(user);
        return RegisterResponse.builder()
                .isSuccessful(true)
                .username(user.getUsername())
                .build();
    }

    @Override
    public LoginResponse login(User user) {
        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(String userEmail) throws UserNotExistException{
        var user = userRepository.findByEmail(userEmail).orElseThrow(UserNotExistException::new);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }
}

