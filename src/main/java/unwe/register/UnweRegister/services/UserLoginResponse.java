package unwe.register.UnweRegister.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import unwe.register.UnweRegister.dtos.user.UserResponse;

@Data
@AllArgsConstructor
public class UserLoginResponse {

    private String token;

    private UserResponse userResponse;

}
