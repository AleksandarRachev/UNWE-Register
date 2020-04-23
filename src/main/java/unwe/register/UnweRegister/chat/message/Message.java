package unwe.register.UnweRegister.chat.message;

import lombok.*;
import unwe.register.UnweRegister.chat.WebSocketUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Message {

    private WebSocketUser user;

    @NonNull
    private MessageType type;

    private String data;

    private String room;

}
