package unwe.register.UnweRegister.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unwe.register.UnweRegister.dtos.events.EventResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCatalogResponse {

    private List<EventResponse> events;

    private Long maxElements;

}
