package unwe.register.UnweRegister.dtos.agreements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementRequest {

    @NotBlank(message = "Employer not selected!")
    private String employerId;

    @NotBlank(message = "Date of agreement not selected!")
    private Long date;

    @NotBlank(message = "Title must not be empty!")
    @Size(max = 200, message = "Title must not be more than 200 symbols!")
    private String title;

    @NotBlank(message = "Description must not be empty!")
    @Size(max = 2000, message = "Description must not be more than 2000 symbols!")
    private String description;

}
