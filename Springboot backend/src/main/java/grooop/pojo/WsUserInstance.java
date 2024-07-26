package grooop.pojo;


import jakarta.websocket.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsUserInstance {
    private String userId;
    private String username;
    private String progress;
}
