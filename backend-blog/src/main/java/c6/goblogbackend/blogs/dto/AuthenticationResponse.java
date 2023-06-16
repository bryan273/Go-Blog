package c6.goblogbackend.blogs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String content;
    private String username;
    private int userId;
    private boolean isAuthenticated;

}
