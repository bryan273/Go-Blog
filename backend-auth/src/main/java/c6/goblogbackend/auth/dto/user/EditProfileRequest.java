package c6.goblogbackend.auth.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class EditProfileRequest {
    private String username;
    private String fullname;
    private String description;
}
