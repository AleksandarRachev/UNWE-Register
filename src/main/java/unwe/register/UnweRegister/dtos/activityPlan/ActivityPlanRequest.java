package unwe.register.UnweRegister.dtos.activityPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlanRequest {

    @NotBlank(message = "Description must not be empty!")
    @Size(max = 2500, message = "Description must not be more than 2500 symbols!")
    private String description;

    @NotNull(message = "Agreement number must not be empty!")
    private Long agreementNumber;

}
