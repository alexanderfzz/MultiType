package grooop.service;

import grooop.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> queryAllUsers(Map<String, String> headers);
    User queryUserById(int id);
    User queryUserByUsername(String username);
    User addUser(User user, Map<String, String> headers);
    User updateUser(User user, Map<String, String> headers, int id);
    boolean deleteUserById(int id, Map<String, String> headers);
    User updateUserAfterRace(User user);
}
