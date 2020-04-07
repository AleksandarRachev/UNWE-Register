package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.services.EventService;
import unwe.register.UnweRegister.services.UserService;

@RestController
@RequestMapping("/images")
@CrossOrigin
public class ImageController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @GetMapping(value = "/user/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserPicture(userId));
    }

    @GetMapping(value = "/event/{eventId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> getEventPicture(@PathVariable("eventId") String eventId){
        return ResponseEntity.ok(eventService.getEventPicture(eventId));
    }
}
