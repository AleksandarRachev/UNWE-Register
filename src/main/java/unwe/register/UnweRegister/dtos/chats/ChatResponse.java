package unwe.register.UnweRegister.dtos.chats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String uid;

    private String employerFirstName;

    private String employerLastName;

    private String coordinatorFirstName;

    private String coordinatorLastName;

}
