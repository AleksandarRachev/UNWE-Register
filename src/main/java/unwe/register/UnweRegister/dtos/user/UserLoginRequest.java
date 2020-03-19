package unwe.register.UnweRegister.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unwe.register.UnweRegister.dtos.interfaces.UserAuthenticationRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest implements UserAuthenticationRequest {

    private String email;

    private String password;

}
