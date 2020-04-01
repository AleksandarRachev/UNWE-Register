package unwe.register.UnweRegister.dtos.activityPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPlansCatalogResponse {

    private List<ActivityPlanResponse> activityPlans;

    private Long maxElements;

}
