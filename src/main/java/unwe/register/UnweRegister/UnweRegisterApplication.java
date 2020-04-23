package unwe.register.UnweRegister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import unwe.register.UnweRegister.chat.ChatServer;
import unwe.register.UnweRegister.services.UserService;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class UnweRegisterApplication {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(UnweRegisterApplication.class, args);
    }

    @PostConstruct
    public void init() {
        ChatServer.ChatServerApp(userService);
    }

}
