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
    public ResponseEntity<EventResponse> addEvent(
            @RequestPart(value = "image", required = false) MultipartFile multipartFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("activityPlanId") String activityPlanId,
            @RequestAttribute("userId") String userId) throws IOException {
        return ResponseEntity.ok(eventService.addEvent(title, description, activityPlanId, multipartFile, userId));
    }

    @PutMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<EventResponse> editEvent(
            @RequestPart(value = "image", required = false) MultipartFile multipartFile,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("activityPlanId") String activityPlanId,
            @RequestParam("eventId") String eventId,
            @RequestAttribute("userId") String userId) throws IOException {
        return ResponseEntity.ok(eventService.editEvent(title, description, activityPlanId, eventId, multipartFile, userId));
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventCatalogResponse> getAllEvents(
            @RequestParam("page") int page,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") String sort) {
        return ResponseEntity.ok(eventService.getEvents(page, search, sort));
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventResponse> getEventById(@PathVariable("eventId") String eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") String eventId,
                                              @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(eventService.deleteEvent(eventId, userId));
    }
}
