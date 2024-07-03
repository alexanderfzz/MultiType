package grooop.controller;

import grooop.JwtUtil;
import grooop.RaceUtil;
import grooop.mapper.QuoteMapper;
import grooop.mapper.UserMapper;
import grooop.mapper.RecordMapper;
import grooop.pojo.Quote;
import grooop.pojo.User;
import grooop.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static grooop.controller.LogController.urlPrefix;

@RestController
@RequestMapping(urlPrefix)
public class LogController {

    public static final String urlPrefix = "/log";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuoteMapper quoteMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private RaceUtil raceUtil;



    // GET
    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsers(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            List<User> users = userMapper.queryAllUsers();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = userMapper.queryUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/quote")
    public ResponseEntity<List<Quote>> getQuotes() {
        List<Quote> quotes = quoteMapper.queryAllQuotes();
        return ResponseEntity.ok(quotes);
    }

    @GetMapping("/quote/{id}")
    public ResponseEntity<Quote> getQuoteById(@PathVariable("id") int id) {
        Quote quote = quoteMapper.queryQuoteById(id);
        return ResponseEntity.ok(quote);
    }

    @GetMapping("/record")
    public ResponseEntity<List<Record>> getRecords() {
        List<Record> records = recordMapper.queryAllRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/record/{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable("id") int id) {
        Record record = recordMapper.queryRecordById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/record/user/{userId}")
    public ResponseEntity<List<Record>> getRecordsByUserId(@PathVariable("userId") int userId) {
        List<Record> records = recordMapper.queryUserRecords(userId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/record/quote/{quoteId}")
    public ResponseEntity<List<Record>> getRecordsByQuoteId(@PathVariable("quoteId") int quoteId) {
        List<Record> records = recordMapper.queryQuoteRecords(quoteId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/record/userquote/{userId}/{quoteId}")
    public ResponseEntity<List<Record>> getUserQuoteRecords(@PathVariable("userId") int userId, @PathVariable("quoteId") int quoteId) {
        List<Record> records = recordMapper.queryUserQuoteRecords(userId, quoteId);
        return ResponseEntity.ok(records);
    }






    //POST
    @PostMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestHeader Map<String, String> headers, @PathVariable int id) {
        User queriedUser = userMapper.queryUserById(id);
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {

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
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @PostMapping("/quote/{id}")
    public ResponseEntity<Quote> updateQuote(@RequestBody Quote quote, @RequestHeader Map<String, String> headers, @PathVariable int id) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            quote.setLength(quote.getContent().length());
            quote.setId(id);
            quoteMapper.updateQuote(quote);
            return ResponseEntity.status(HttpStatus.OK).body(quote);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    //PUT
    @PutMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User user, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            user.setAverage(user.getAverage()==null ? 0 : user.getAverage());
            user.setRaces(user.getRaces()==null ? 0 : user.getRaces());
            userMapper.addUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @PutMapping("/quote")
    public ResponseEntity<Quote> addQuote(@RequestBody Quote quote, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            quote.setLength(quote.getContent().length());
            quoteMapper.addQuote(quote);
            return ResponseEntity.status(HttpStatus.OK).body(quote);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @PutMapping("/record")
    public ResponseEntity<Record> addRecord(@RequestBody Record record, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            raceUtil.updateUserQuoteRecords(record.getUserId(), record.getQuoteId());
            recordMapper.addRecord(record);
            return ResponseEntity.status(HttpStatus.OK).body(record);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    //DELETE
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            userMapper.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/quote/{id}")
    public ResponseEntity<String> deleteQuoteById(@PathVariable("id") int id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            quoteMapper.deleteQuote(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/record/{id}")
    public ResponseEntity<String> deleteRecordById(@PathVariable("id") int id, @RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            recordMapper.deleteRecord(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



}
