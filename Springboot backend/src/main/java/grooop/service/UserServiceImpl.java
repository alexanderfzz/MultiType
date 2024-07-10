package grooop.service;

import grooop.JwtUtil;
import grooop.mapper.UserMapper;
import grooop.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecordServiceImpl recordService;

    @Override
    public List<User> queryAllUsers(Map<String, String> headers) {
        String token = headers.get("authorization");
        List<User> users = null;
        if (JwtUtil.validateToken(token)) {
            users = userMapper.queryAllUsers();
        }
        return users;
    }

    @Override
    public User queryUserById(int id) {
        User user = userMapper.queryUserById(id);
        return user;
    }

    @Override
    public User queryUserByUsername(String username) {
        User user = userMapper.queryUserByUsername(username);
        return user;
    }

    @Override
    public User addUser(User user, Map<String, String> headers) {
        String token = headers.get("authorization");
          if (JwtUtil.validateToken(token)) {
            if (user.getAverage()==null) {
                user.setAverage(0.00);
            }
            if (user.getRaces()==null) {
                user.setRaces(0);
            }
            userMapper.addUser(user);
            return user;
          }
          return null;
    }

    @Override
    public User updateUser(User user, Map<String, String> headers, int id) {
        String token = headers.get("authorization");
        boolean validated = JwtUtil.validateToken(token);
        if (validated) {
            User queriedUser = userMapper.queryUserById(id);
            if (user.getPassword()==null) {
                user.setPassword(queriedUser.getPassword());
            }
            if (user.getEmail()==null) {
                user.setEmail(queriedUser.getEmail());
            }
            if (user.getAverage()==null) {
                user.setAverage(queriedUser.getAverage());
            }
            if (user.getRaces()==null) {
                user.setRaces(queriedUser.getRaces());
            }
            userMapper.updateUser(user);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User updateUserAfterRace(User user) {
        return null;

    }

    @Override
    public boolean deleteUserById(int id, Map<String, String> headers) {
        String token = headers.get("authorization");
        boolean validated = JwtUtil.validateToken(token);
        if (validated) {
            recordService.deleteUserRecords(id, headers);
            userMapper.deleteUser(id);
        }
        return validated;
    }


}
