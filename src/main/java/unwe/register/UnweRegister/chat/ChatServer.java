package unwe.register.UnweRegister.chat;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unwe.register.UnweRegister.chat.message.Message;
import unwe.register.UnweRegister.chat.message.MessageType;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.services.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ChatServer extends WebSocketServer {

    private static final String CHAT_SERVER_STARTED = "Chat server on port(s): ";
    private static final String WEB_SOCKET_INFO = " (ws) with context path ''";
    private static final String ERROR = "ERROR from ";
    private static final String MESSAGE_ERROR = "Problem with message";
    private static final String CONNECTION_CLOSED = "Closed connection to ";
    private static final String CONNECTION_OPEN = "New connection from ";
    private static final int CHAT_PORT = 9000;

    static Logger logger = LoggerFactory.getLogger(ChatServer.class);

    private final HashMap<String, List<WebSocket>> connections;

    private final ObjectMapper mapper;

    private final UserService userService;

    @Autowired
    public ChatServer(UserService userService) {
        super(new InetSocketAddress(CHAT_PORT));
        this.connections = new HashMap<>();
        this.mapper = new ObjectMapper();
        this.userService = userService;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

        try {
            Message message = new Message(MessageType.USER_CONNECTED);
            webSocket.send(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info(CONNECTION_OPEN + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

        for (List<WebSocket> webSocket : connections.values()) {
            webSocket.remove(conn);
        }

        logger.info(CONNECTION_CLOSED + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            Message msg = mapper.readValue(message, Message.class);
            switch (msg.getType()) {
                case USER_JOINED:
                    addUser(msg.getUser().getId(), conn, msg.getRoom());
                    break;
                case TEXT_MESSAGE:
                    broadcastMessage(msg, msg.getRoom());
            }
        } catch (IOException e) {
            logger.info(MESSAGE_ERROR);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

        if (conn != null) {
            connections.clear();
        }
        assert conn != null;
        logger.error(ERROR + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    private void broadcastMessage(Message msg, String room) throws JsonProcessingException {
        msg.setRoom(room);
        String messageJson = mapper.writeValueAsString(msg);
        for (WebSocket sock : connections.get(room)) {
            sock.send(messageJson);
        }
    }

    private void addUser(String id, WebSocket conn, String room) throws JsonProcessingException {
        connections.computeIfAbsent(room, k -> new ArrayList<>());
        connections.get(room).add(conn);

        User user = userService.getUser(id);

        broadcastUserActivityMessage(new WebSocketUser(user.getUid(), user.getFirstName() + " " + user.getLastName()), MessageType.USER_JOINED, room);
    }

    private void broadcastUserActivityMessage(WebSocketUser user, MessageType messageType, String room)
            throws JsonProcessingException {

        Message newMessage = new Message();

        newMessage.setUser(user);
        newMessage.setType(messageType);
        broadcastMessage(newMessage, room);
    }

    public static void ChatServerApp(UserService userService) {
        new ChatServer(userService).start();
        logger.info(CHAT_SERVER_STARTED + CHAT_PORT + WEB_SOCKET_INFO);
    }

}