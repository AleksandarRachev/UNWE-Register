package unwe.register.UnweRegister.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private String uid;

    private String title;

    private String description;

    private String activityPlanUid;

    private String imageUrl;

}
