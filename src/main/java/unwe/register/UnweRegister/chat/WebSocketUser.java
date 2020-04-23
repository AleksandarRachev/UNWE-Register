package unwe.register.UnweRegister.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketUser {

    private String id;
    private String name;

    public WebSocketUser(String name) {
        this.name = name;
        id = UUID.randomUUID().toString();
    }
}
