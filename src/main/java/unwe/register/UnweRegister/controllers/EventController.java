package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unwe.register.UnweRegister.dtos.events.EventCatalogResponse;
import unwe.register.UnweRegister.dtos.events.EventResponse;
import unwe.register.UnweRegister.services.EventService;

import java.io.IOException;

@RestController
@RequestMapping("/events")
@CrossOrigin("*")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<EventResponse> editUserProfile(@RequestPart(value = "image", required = false) MultipartFile multipartFile,
                                                         @RequestParam("title") String title,
                                                         @RequestParam("description") String description,
                                                         @RequestParam("activityPlanId") String activityPlanId,
                                                         @RequestAttribute("userId") String userId) throws IOException {
        return ResponseEntity.ok(eventService.addEvent(title, description, activityPlanId, multipartFile, userId));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventCatalogResponse> getAllEvents(@RequestParam("page") int page) {
        return ResponseEntity.ok(eventService.getEvents(page));
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") String eventId,
                                              @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(eventService.deleteEvent(eventId, userId));
    }
}
