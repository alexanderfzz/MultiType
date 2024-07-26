package grooop.service.impl;

import grooop.JwtUtil;
import grooop.mapper.RecordMapper;
import grooop.pojo.Record;
import grooop.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public List<Record> queryAllRecords(Map<String, String> headers) {
        String token = headers.get("authorization");
        List<Record> records = null;
        if (JwtUtil.validateToken(token)) {
            records = recordMapper.queryAllRecords();
        }
        return records;
    }

    @Override
    public Record queryRecordById(int recordId, Map<String, String> headers) {
        String token = headers.get("authorization");
        Record record = null;
        if (JwtUtil.validateToken(token)) {
            record = recordMapper.queryRecordById(recordId);
        }
        return record;
    }

    @Override
    public List<Record> queryUserRecords(int userId, Map<String, String> headers) {
        String token = headers.get("authorization");
        List<Record> records = null;
        if (JwtUtil.validateToken(token)) {
            records = recordMapper.queryUserRecords(userId);
        }
        return records;
    }

    @Override
    public List<Record> queryQuoteRecords(int quoteId, Map<String, String> headers) {
        String token = headers.get("authorization");
        List<Record> records = null;
        if (JwtUtil.validateToken(token)) {
            records = recordMapper.queryQuoteRecords(quoteId);
        }
        return records;
    }

    @Override
    public List<Record> queryUserQuoteRecords(int userId, int quoteId, Map<String, String> headers) {
        String token = headers.get("authorization");
        List<Record> records = null;
        if (JwtUtil.validateToken(token)) {
            records = recordMapper.queryUserQuoteRecords(userId, quoteId);
        }
        return records;
    }

    @Override
    public Record addRecord(Record record, Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            List<Record> records = recordMapper.queryUserQuoteRecords(record.getUserId(), record.getQuoteId());
            int counter = records.size() - 10;

            for(Record recordIter : records) {
                if (counter < 0) {
                    break;
                }
                this.recordMapper.deleteRecord(recordIter.getId());
            }
            recordMapper.addRecord(record);
            return record;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteRecord(int recordId, Map<String, String> headers) {
        String token = headers.get("authorization");
        boolean validated = JwtUtil.validateToken(token);
        if (validated) {
            recordMapper.deleteRecord(recordId);
        }
        return validated;
    }

    @Override
    public boolean deleteUserRecords(int userId, Map<String, String> headers) {
        String token = headers.get("authorization");
        boolean validated = JwtUtil.validateToken(token);
        if (validated) {
            recordMapper.deleteUserRecords(userId);
        }
        return validated;
    }

    @Override
    public boolean deleteQuoteRecords(int quoteId, Map<String, String> headers) {
        String token = headers.get("authorization");
        boolean validated = JwtUtil.validateToken(token);
        if (validated) {
            recordMapper.deleteQuoteRecords(quoteId);
        }
        return validated;
    }
}
