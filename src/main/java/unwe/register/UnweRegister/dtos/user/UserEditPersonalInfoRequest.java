package unwe.register.UnweRegister.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditPersonalInfoRequest {

//    @NotBlank(message = "Email must not be empty!")
//    private String email;
//
//    @NotBlank(message = "First name must not be empty!")
//    private String firstName;
//
//    @NotBlank(message = "Last name must not be blank!")
//    private String lastName;

    private String address;

    private String contactPerson;
}
