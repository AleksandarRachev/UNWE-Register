package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.dtos.chats.ChatResponse;
import unwe.register.UnweRegister.services.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chats")
@CrossOrigin("*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/{chatUserId}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('EMPLOYER')")
    public ResponseEntity<String> createChat(@PathVariable("chatUserId") String chatUserId,
                                                @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(chatService.createChatForUser(chatUserId, userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('EMPLOYER')")
    public ResponseEntity<List<ChatResponse>> getChats(@RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(chatService.getChatsForUser(userId));
    }
}
