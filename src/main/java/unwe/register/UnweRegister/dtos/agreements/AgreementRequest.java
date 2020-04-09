package unwe.register.UnweRegister.dtos.agreements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementRequest {

    @NotBlank(message = "Employer not selected!")
    private String employerId;

    @NotNull(message = "Date of agreement not selected!")
    private Long date;

    @NotBlank(message = "Title must not be empty!")
    @Size(max = 250, message = "Title must not be more than 200 symbols!")
    private String title;

    @NotBlank(message = "Description must not be empty!")
    @Size(max = 2500, message = "Description must not be more than 2500 symbols!")
    private String description;

}
