package grooop.mapper;

import grooop.pojo.Record;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RecordMapper {

    List<Record> queryAllRecords();
    Record queryRecordById(int recordId);
    List<Record> queryUserRecords(int userId);
    List<Record> queryQuoteRecords(int quoteId);
    List<Record> queryUserQuoteRecords(int userId, int quoteId);
    void addRecord(Record record);
    void updateRecord(Record record);
    void deleteRecord(int recordId);
    void deleteUserRecords(int userId);
    void deleteQuoteRecords(int quoteId);
}
