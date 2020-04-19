package unwe.register.UnweRegister.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unwe.register.UnweRegister.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String uid;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Role role;

    private String address;

    private String contactPerson;

    private String companyName;

    private String imageUrl;

}
