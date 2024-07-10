package grooop.service;

import grooop.pojo.Record;

import java.util.List;
import java.util.Map;

public interface RecordService {

    List<Record> queryAllRecords(Map<String, String> headers);
    Record queryRecordById(int recordId, Map<String, String> headers);
    List<Record> queryUserRecords(int userId, Map<String, String> headers);
    List<Record> queryQuoteRecords(int quoteId, Map<String, String> headers);
    List<Record> queryUserQuoteRecords(int userId, int quoteId, Map<String, String> headers);
    Record addRecord(Record record, Map<String, String> headers);
    boolean deleteRecord(int recordId, Map<String, String> headers);
    boolean deleteUserRecords(int userId, Map<String, String> headers);
    boolean deleteQuoteRecords(int quoteId, Map<String, String> headers);
}
