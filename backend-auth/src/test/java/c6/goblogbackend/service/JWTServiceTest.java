package c6.goblogbackend.service;

import c6.goblogbackend.auth.model.User;
import c6.goblogbackend.auth.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JWTServiceTest {

    private JWTService jwtService;

    @Mock
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JWTService();
        jwtService.setSecretKey("Q09NUEZFU1QxNXttb25lX3R3b2ZhY2VfZmFyYXdheX0=");
    }

    @Test
    void testExtractSubject() {
        when(mockUser.getEmail()).thenReturn("email@example.com");
        User user = User.builder().id(1).email("email@example.com").build();
        String token = jwtService.generateToken(user);

        String subject = jwtService.extractSubject(token);

        assertEquals("email@example.com", subject);
    }

    @Test
    void testGenerateToken() {
        when(mockUser.getUsername()).thenReturn("john_doe");
        when(mockUser.getId()).thenReturn(123);

        String token = jwtService.generateToken(mockUser);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testIsTokenValid_ValidTokenAndMatchingUser_ReturnsTrue() {
        when(mockUser.getEmail()).thenReturn("email@example.com");
        User user = User.builder().id(1).email("email@example.com").build();
        String token = jwtService.generateToken(user);
        boolean isValid = jwtService.isTokenValid(token, mockUser);

        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_ExpiredToken_ReturnsFalse() {
        when(mockUser.getEmail()).thenReturn("email@example.com");

        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() - 1000 * 60 * 60));

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, mockUser));
    }

    @Test
    void testIsTokenValid_InvalidToken_ReturnsFalse() {
        when(mockUser.getEmail()).thenReturn("email@example.com");

        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() + 100000 * 60 * 60));
        token = token.substring(0,token.length()-1);

        String finalToken = token;
        assertThrows(SignatureException.class, () -> jwtService.isTokenValid(finalToken, mockUser));
    }

    @Test
    void testIsTokenExpired_NotExpiredToken_ReturnsFalse() {
        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testIsTokenExpired_ExpiredToken_ReturnsTrue() {
        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() - 1000 * 60 * 60));

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenExpired(token));
    }

    @Test
    void testExtractClaim() {
        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals("email@example.com", subject);
    }

    @Test
    void testExtractAllClaims_ValidToken_ReturnsClaims() {
        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
    }

    @Test
    void testGetSignInKey() {
        assertNotNull(jwtService.getSignInKey());
    }

    @Test
    void testVerify_ValidToken_ReturnsTrue() {
        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        boolean isVerified = jwtService.verify(token);

        assertTrue(isVerified);
    }

    @Test
    void testVerify_InvalidToken_ReturnsFalse() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMSIsInVzZXJuYW1lIjoibmVvemFwIiwic3ViIjoibmVvemFwQGdtYWlsLmNvIiwiaWF0IjoxNjg0NzM0MjgxLCJleHAiOjE2ODQ4MjA2ODF9.p2mhF2auyEDDuK6TOEmulUGkbTk-XZPXDi7KPOJ4aC";

        assertThrows(SignatureException.class, () -> jwtService.verify(token));
    }

    @Test
    void testParseBody_ValidToken_ReturnsClaims() {
        String token = jwtService.generateSampleToken("email@example.com", new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        Claims claims = jwtService.parseBody(token);

        assertNotNull(claims);
    }

    @Test
    void testParseBody_InvalidToken_ThrowsException() {
        String token = "invalid-token";

        assertThrows(MalformedJwtException.class, () -> jwtService.parseBody(token));
    }

    @Test
    void testGenerateTokenWithExtraClaims() {
        when(mockUser.getUsername()).thenReturn("john_doe");
        when(mockUser.getId()).thenReturn(123);

        Map<String, String> extraClaims = new HashMap<>();
        extraClaims.put("role", "admin");

        String token = jwtService.generateToken(extraClaims, mockUser);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testIsTokenValid_ValidTokenWithExtraClaims_ReturnsTrue() {
        when(mockUser.getEmail()).thenReturn("email@example.com");
        User user = User.builder().id(1).email("email@example.com").build();

        Map<String, String> extraClaims = new HashMap<>();
        extraClaims.put("role", "admin");

        String token = jwtService.generateToken(extraClaims, user);
        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void testGenerateSampleToken() {
        String subject = "email@example.com";
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // Tambahkan waktu yang sesuai untuk expiration

        String token = jwtService.generateSampleToken(subject, expiration);

        assertNotNull(token);

        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("email@example.com", claims.getSubject());
        assertEquals("john_doe", claims.get("username"));
        assertEquals("123", claims.get("user_id"));
    }

    @Test
    void testExtractSubject_ValidToken_ReturnsSubject() {
        when(mockUser.getEmail()).thenReturn("email@example.com");
        User user = User.builder().id(1).email("email@example.com").build();
        String token = jwtService.generateToken(user);

        String subject = jwtService.extractSubject(token);
        assertNotNull(subject);
    }

    @Test
    void testExtractSubject_InvalidToken_ReturnsNull() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMTIzIiwidXNlcm5hbWUiOiJqb2huX2RvZSIsImlhdCI6MTY4NDkzMTQzOSwiZXhwIjoxNjg1MDE3ODM5fQ.YB_-jt-9bkPbD1TKQYD7LDcl_9Z9pk-56JLFYFP19s";

        assertThrows(SignatureException.class, () -> jwtService.extractSubject(token));
    }
}
