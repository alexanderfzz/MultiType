package grooop.controller;

import grooop.pojo.User;
import grooop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    // GET
    @GetMapping("")
    public ResponseEntity<List<User>> queryAllUsers(@RequestHeader Map<String, String> headers) {
        List<User> users = userService.queryAllUsers(headers);
        if (users != null) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> queryUserById(@PathVariable("id") int id) {
        User user = userService.queryUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    // POST
    @PostMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestHeader Map<String, String> headers, @PathVariable int id) {
        User updatedUser = userService.updateUser(user, headers, id);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // PUT
    @PutMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user, @RequestHeader Map<String, String> headers) {
        User newUser = userService.addUser(user, headers);
        if (newUser != null) {
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int id, @RequestHeader Map<String, String> headers) {
        boolean operationSucceeded = userService.deleteUserById(id, headers);
        if (operationSucceeded) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
