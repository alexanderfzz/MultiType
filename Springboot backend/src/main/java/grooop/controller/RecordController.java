package grooop.controller;

import grooop.pojo.Record;
import grooop.service.impl.RecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log/record")
public class RecordController {

    @Autowired
    private RecordServiceImpl recordService;



    @GetMapping("")
    public ResponseEntity<List<Record>> queryAllRecords(@RequestHeader Map<String, String> headers) {
        List<Record> records = recordService.queryAllRecords(headers);
        if (records != null) {
            return ResponseEntity.ok(records);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<Record> queryRecordById(@PathVariable("recordId") int recordId, @RequestHeader Map<String, String> headers) {
        Record record = recordService.queryRecordById(recordId, headers);
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Record>> getRecordsByUserId(@PathVariable("userId") int userId, @RequestHeader Map<String, String> headers) {
        List<Record> records = recordService.queryUserRecords(userId, headers);
        if (records != null) {
            return ResponseEntity.ok(records);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/quote/{quoteId}")
    public ResponseEntity<List<Record>> getRecordsByQuoteId(@PathVariable("quoteId") int quoteId, @RequestHeader Map<String, String> headers) {
        List<Record> records = recordService.queryQuoteRecords(quoteId, headers);
        if (records != null) {
            return ResponseEntity.ok(records);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/userquote/{userId}/{quoteId}")
    public ResponseEntity<List<Record>> getUserQuoteRecords(@PathVariable("userId") int userId, @PathVariable("quoteId") int quoteId, @RequestHeader Map<String, String> headers) {
        List<Record> records = recordService.queryUserQuoteRecords(userId, quoteId, headers);
        if (records != null) {
            return ResponseEntity.ok(records);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @PutMapping("")
    public ResponseEntity<Record> addRecord(@RequestBody Record record, @RequestHeader Map<String, String> headers) {
        Record newRecord = recordService.addRecord(record, headers);
        if (newRecord != null) {
            return ResponseEntity.ok(newRecord);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteRecordById(@PathVariable("recordId") int recordId, @RequestHeader Map<String, String> headers) {
        boolean operationSucceeded = recordService.deleteRecord(recordId, headers);
        if (operationSucceeded) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserRecords(@PathVariable("userId") int userId, @RequestHeader Map<String, String> headers) {
        boolean operationSucceeded = recordService.deleteUserRecords(userId, headers);
        if (operationSucceeded) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{quoteId}")
    public ResponseEntity<String> deleteQuoteRecords(@PathVariable("quoteId") int quoteId, @RequestHeader Map<String, String> headers) {
        boolean operationSucceeded = recordService.deleteQuoteRecords(quoteId, headers);
        if (operationSucceeded) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
