package grooop.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grooop.pojo.WsUserInstance;
import grooop.service.impl.QuoteServiceImpl;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@ServerEndpoint("/race/{roomId}")
@Component
public class RaceServerEndpoint {
    private static final ConcurrentHashMap<String, RaceServerEndpoint> USER_CLIENT_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, WsUserInstance>> roomSectionedUserProgress = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicInteger> roomRankings = new ConcurrentHashMap<>();
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private Session session;
    private String userId;
    private String roomId;

    private String username;

    private static QuoteServiceImpl quoteService;

    @Autowired
    private void setQuoteService(QuoteServiceImpl quoteService) {
        RaceServerEndpoint.quoteService = quoteService;
    }

    private static Map<String, Function> functionMap = new HashMap<>();


    public int echo(int message) {
        return message;
    }




    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) throws JsonProcessingException {
        this.roomId = roomId;
        String sessionId = session.getId();
        System.out.println("Connection established, sessionId: "+sessionId);
        this.session = session;
        if (USER_CLIENT_MAP.containsKey(sessionId)) {
            USER_CLIENT_MAP.remove(sessionId);
        } else {
            onlineCount.incrementAndGet();
        }
        USER_CLIENT_MAP.put(sessionId, this);


    }


    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        System.out.println("Connection closed, sessionId: "+sessionId);
        if (USER_CLIENT_MAP.containsKey(sessionId)) {
            USER_CLIENT_MAP.remove(sessionId);
            onlineCount.decrementAndGet();
        }


        if (roomSectionedUserProgress.containsKey(this.roomId)) {
            ConcurrentHashMap<String, WsUserInstance> roomMap = roomSectionedUserProgress.get(this.roomId);
            roomMap.remove(this.userId);
            if (roomMap.isEmpty()) {
                roomSectionedUserProgress.remove(this.roomId);
            }
        }

        roomLog();

    }


    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        String sessionId = session.getId();
        System.out.println("SessionId: " + session.getId() + ", received client's message, content: " + message);

        Map messageMap = objectMapper.readValue(message, Map.class);
        String command = (String) messageMap.get("command");
        Object content = messageMap.get("content");

        switch (command) {
            case "register" -> register((Map) content);
            case "startTest" -> startTest();
            case "getStartTimer" -> getTimer();
            case "endTest" -> endTest();
            case "progressUpdate" -> progressUpdate((Map) content);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }



    private void register(Map<String, Object> content) throws JsonProcessingException {
        this.userId = String.valueOf(content.get("userId"));
        this.username = String.valueOf(content.get("username"));

        ConcurrentHashMap<String, WsUserInstance> roomMap;
        if (!roomSectionedUserProgress.containsKey(this.roomId)) {
            roomMap = new ConcurrentHashMap<>();
            roomSectionedUserProgress.put(this.roomId, roomMap);
        } else {
            roomMap = roomSectionedUserProgress.get(this.roomId);
        }
        roomMap.put(this.userId, new WsUserInstance(this.userId, this.username, "0%"));

        if (!roomRankings.containsKey(this.roomId)) {
            AtomicInteger roomRanking = new AtomicInteger(0);
            roomRankings.put(this.roomId, roomRanking);
        }


//        if (roomMap.size() >= 2) {
//            primeTest();
//        }

        roomLog();
    }

    private void roomLog() {
        System.out.println("------------------------------------------------------------------------");
        System.out.println("------------------------------------------------------------------------");
        System.out.println(roomSectionedUserProgress);
        System.out.println("------------------------------------------------------------------------");
    }

    private void primeTest() throws JsonProcessingException {
        Map<String, Object> primeMap = new HashMap<>();
        primeMap.put("command", "primeTest");
        Map<String, Object> contentMap = new HashMap<>();
        primeMap.put("content", contentMap);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenSecondsLater = now.plusSeconds(10);
        ZonedDateTime zonedDateTime = tenSecondsLater.atZone(ZoneId.systemDefault());
        long timestamp = zonedDateTime.toEpochSecond();
        contentMap.put("startTime", timestamp);

        sendMessageByRoomId(objectMapper.writeValueAsString(primeMap), this.roomId);
    }


    private void startTest() throws JsonProcessingException {
        Random random = new Random();
        Map<String, Object> startTestMap = new HashMap<>();
        startTestMap.put("command", "startTest");
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("selector", String.valueOf(random.nextInt(quoteService.queryTableSize())));
        progressRankingReset();
        contentMap.put("progress", roomSectionedUserProgress.get(this.roomId));
        startTestMap.put("content", contentMap);
        String returnMessage = objectMapper.writeValueAsString(startTestMap);
        sendToAll(returnMessage);
    }


    private void getTimer() throws JsonProcessingException {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> timerMap = new HashMap<>();
        timerMap.put("command", "startTimer");
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("timestamp", String.valueOf(timestamp));
        timerMap.put("content", contentMap);
        sendMessageByRoomId(objectMapper.writeValueAsString(timerMap), this.roomId);
    }

    private void endTest() throws JsonProcessingException {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> endTestMap = new HashMap<>();
        endTestMap.put("command", "finishTest");
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("timestamp", String.valueOf(timestamp));
        contentMap.put("dateTime", String.valueOf(objectMapper.writeValueAsString(new Date())));
        AtomicInteger currentRanking = roomRankings.get(this.roomId);
        contentMap.put("placement", String.valueOf(currentRanking));
        currentRanking.set(currentRanking.intValue() + 1);
        endTestMap.put("content", contentMap);
        sendMessageBySessionId(objectMapper.writeValueAsString(endTestMap), this.session.getId());
    }

    private void progressUpdate(Map content) throws JsonProcessingException {
        Map<String, Object> returnProgressMap = new HashMap<>();
        returnProgressMap.put("command", "progressUpdate");
        Map<String, Object> contentMap = new HashMap<>();

        ConcurrentHashMap<String, WsUserInstance> roomMap = roomSectionedUserProgress.get(String.valueOf(this.roomId));

        roomMap.get(this.userId).setProgress((String) content.get("progress"));
        contentMap.put("progress", roomMap);


        returnProgressMap.put("content", contentMap);
        sendMessageByRoomId(objectMapper.writeValueAsString(returnProgressMap), this.roomId);

    }


    private void progressRankingReset() {
        ConcurrentHashMap<String, WsUserInstance> roomMap = roomSectionedUserProgress.get(this.roomId);
        for (String key : roomMap.keySet()) {
            roomMap.get(key).setProgress("0%");
        }
        AtomicInteger roomRanking = roomRankings.get(this.roomId);
        roomRanking.set(1);

    }

    private void sendToAll(String message) {
        USER_CLIENT_MAP.values().forEach((item -> {
            item.sendMessage(message);
        }));
        System.out.println("sent to all: "+message);
    }


    private void sendToAllOthers(String message) {
        USER_CLIENT_MAP.values().forEach(item -> {
            if (!item.equals(this)) {
                item.sendMessage(message);
            }
        });
    }


    private void sendMessageByRoomId(String message, String targetRoomId) {
        USER_CLIENT_MAP.values().forEach(item -> {
            if (item.roomId.equals(targetRoomId)) {
                item.sendMessage(message);
            }
        });
    }


    private void sendMessageByRoomIdNotMe(String message, String targetRoomId) {
        USER_CLIENT_MAP.values().forEach(item -> {
            if (item.roomId.equals(targetRoomId) && !item.equals(this)) {
                item.sendMessage(message);
            }
        });
    }


    private void sendMessageBySessionId(String message, String sessionId) {
        RaceServerEndpoint item = USER_CLIENT_MAP.get(sessionId);
        if (item != null) {
            item.sendMessage(message);
            System.out.println("private message sent to "+sessionId+": "+message);
        }
    }


    private void sendMessage(String message) {
        try {
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            System.out.println("Something unexpected occurred during websocket's message transmission to the client! " +
                    "Message content: " + message +
                    ", sessionId: " + this.toString() + "____" + e);
        }
    }
}
