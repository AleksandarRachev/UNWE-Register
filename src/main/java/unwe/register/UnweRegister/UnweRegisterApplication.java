package unwe.register.UnweRegister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import unwe.register.UnweRegister.chat.ChatServer;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class UnweRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnweRegisterApplication.class, args);
    }

    @PostConstruct
    public void init() {
        ChatServer.ChatServerApp();
    }

}
