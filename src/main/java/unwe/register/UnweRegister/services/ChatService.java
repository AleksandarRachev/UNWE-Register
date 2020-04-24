package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import unwe.register.UnweRegister.dtos.chats.ChatResponse;
import unwe.register.UnweRegister.entities.Chat;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.enums.Role;
import unwe.register.UnweRegister.exceptions.InvalidOperationException;
import unwe.register.UnweRegister.repositories.ChatRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final String CHAT_CREATED = "Chat created: ";
    private static final String CANNOT_CREATE_CHAT = "Cannot create chat with yourself!";

    private final ChatRepository chatRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    @Autowired
    public ChatService(ChatRepository chatRepository, ModelMapper modelMapper, UserService userService) {
        this.chatRepository = chatRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    public String createChatForUser(String chatUserId, String userId) {

        if(chatUserId.equals(userId)){
            throw new InvalidOperationException(CANNOT_CREATE_CHAT);
        }

        User loggedUser = userService.getUser(userId);

        Chat chat = getChatDependingOnLoggedUserRole(chatUserId, loggedUser);

        return CHAT_CREATED + chat.getUid();
    }

    private Chat createAndGetChatDependingOnRole(String notLoggedUserId, User loggedUser) {
        User user = userService.getUser(notLoggedUserId);
        if (loggedUser.getRole() == Role.COORDINATOR) {
            return chatRepository.save(new Chat(loggedUser, user));
        } else {
            return chatRepository.save(new Chat(user, loggedUser));
        }
    }

    private Chat getChatDependingOnLoggedUserRole(String chatUserId, User loggedUser) {
        Optional<Chat> chat;
        if (loggedUser.getRole() == Role.COORDINATOR) {
            chat = chatRepository.findByEmployerUidAndCoordinatorUid(chatUserId, loggedUser.getUid());
        } else {
            chat = chatRepository.findByEmployerUidAndCoordinatorUid(loggedUser.getUid(), chatUserId);
        }

        return chat.orElseGet(() -> createAndGetChatDependingOnRole(chatUserId, loggedUser));
    }

    public List<ChatResponse> getChatsForUser(String userId) {
        User user = userService.getUser(userId);

        List<Chat> chats;
        if (user.getRole() == Role.COORDINATOR) {
            chats = chatRepository.findAllByCoordinatorUid(userId);
        } else {
            chats = chatRepository.findAllByEmployerUid(userId);
        }

        return chats.stream()
                .map(chat -> modelMapper.map(chat, ChatResponse.class))
                .collect(Collectors.toList());
    }
}
