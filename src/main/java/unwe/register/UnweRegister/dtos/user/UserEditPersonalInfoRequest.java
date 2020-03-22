package unwe.register.UnweRegister.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditPersonalInfoRequest {

    @NotBlank(message = "Email must not be empty!")
    private String email;

    @NotBlank(message = "First name must not be empty!")
    private String firstName;

    @NotBlank(message = "Last name must not be blank!")
    private String lastName;

    @NotNull(message = "Phone must not be empty!")
    @Pattern(regexp = "(\\+)?(359|0)8[789]\\d{1}\\d{3}\\d{3}", message = "Invalid phone number!")
    private String phone;

    private String address;

    private String contactPerson;
}
