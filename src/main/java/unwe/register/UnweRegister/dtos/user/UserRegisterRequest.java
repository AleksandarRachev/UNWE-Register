package unwe.register.UnweRegister.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unwe.register.UnweRegister.dtos.interfaces.UserAuthenticationRequest;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest implements UserAuthenticationRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "Password must not be empty!")
    @Size(min = 6, message = "Length must be at least 6 symbols!")
    private String password;

    @NotBlank(message = "Password must not be empty!")
    @Size(min = 6, message = "Length must be at least 6 symbols!")
    private String repeatPassword;

    @NotNull(message = "Phone must not be empty!")
    @Pattern(regexp = "(\\+)?(359|0)8[789]\\d{1}\\d{3}\\d{3}", message = "Invalid phone number!")
    private String phone;

    @NotBlank(message = "First name must not be empty!")
    private String firstName;

    @NotBlank(message = "Last name must not be empty!")
    private String lastName;

    private String department;


}
