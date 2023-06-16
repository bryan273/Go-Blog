package c6.goblogbackend.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import c6.goblogbackend.auth.model.User;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;




@Service
public class JWTService {

    @Value("${jwt-secret-key}")
    @Setter
    private String secretKey;

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(User userDetails) {
        HashMap<String, String> extraClaims = new HashMap<>();
        extraClaims.put("username", userDetails.getUsername());
        extraClaims.put("user_id", userDetails.getId().toString());

        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, String> extraClaims, User userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, User userDetails) {
        final String subject = extractSubject(token);
        return ((subject!=null) && subject.equals(userDetails.getEmail())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {return extractExpiration(token).before(new Date());}

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean verify(String jwt) {
        Jws<Claims> jws;
        var jwtParser = Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
        jws = jwtParser.parseClaimsJws(jwt);
        return jws != null ;
    }

    public Claims parseBody(String jwt){
        var jwtParser = Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
        return jwtParser.parseClaimsJws(jwt).getBody();
    }

    public String generateSampleToken(String subject, Date expiration) {
        Map<String, String> extraClaims = new HashMap<>();
        extraClaims.put("username", "john_doe");
        extraClaims.put("user_id", "123");

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
