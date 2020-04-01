package unwe.register.UnweRegister.dtos.activityPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlanResponse {

    private String uid;

    private String description;

    private String agreementUid;

    private String agreementNumber;

}
