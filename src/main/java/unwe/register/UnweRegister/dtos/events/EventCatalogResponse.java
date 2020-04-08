package unwe.register.UnweRegister.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCatalogResponse {

    private List<EventResponse> events;

    private Long maxElements;

}
