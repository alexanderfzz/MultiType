package grooop.controller;


import com.auth0.jwt.interfaces.Claim;
import grooop.JwtUtil;
import grooop.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import grooop.pojo.User;
import grooop.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;

import static grooop.JwtUtil.generateToken;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User given) {
        User user = userMapper.queryUserByUsername(given.getUsername());

        if (user != null && user.getPassword().equals(given.getPassword())) {
            String token = generateToken(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse(token));
        } else {
            HashMap<String, Object> failureMap = new HashMap<>();
            failureMap.put("msg", "login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(failureMap);
        }
    }

    @GetMapping("/userInfo")
    public ResponseEntity<Map<String, String>> adminVerification(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        Map<String, Claim> claims = JwtUtil.getTokenClaims(token);
        if (claims == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        } else {
            String id = claims.get("id").toString();
            User queriedUser = userMapper.queryUserById(Integer.parseInt(id));
            HashMap<String, String> userInfo = new HashMap<>();
            userInfo.put("id", id);
            userInfo.put("username", queriedUser.getUsername());
            userInfo.put("average", queriedUser.getAverage().toString());
            userInfo.put("races", queriedUser.getRaces().toString());
            return ResponseEntity.status(HttpStatus.OK).body(userInfo);
        }
    }
}
