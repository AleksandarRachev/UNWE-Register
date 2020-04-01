package unwe.register.UnweRegister.dtos.activityPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlanRequest {

    @NotBlank(message = "Description must not be empty!")
    private String description;

    @NotNull(message = "Agreement number must not be empty!")
    private Long agreementNumber;

}
