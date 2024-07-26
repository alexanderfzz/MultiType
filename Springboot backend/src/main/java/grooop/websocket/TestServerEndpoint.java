package grooop.websocket;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/test/{roomId}")
@Component
public class TestServerEndpoint {

    private Map<String, Session> roomSessions = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        roomSessions.put("guest"+Math.random(), session);
        System.out.println(this+" "+roomId+" "+roomSessions);
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
