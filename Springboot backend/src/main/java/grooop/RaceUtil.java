package grooop;

import grooop.mapper.RecordMapper;
import grooop.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RaceUtil {

    @Autowired
    private RecordMapper recordMapper;

    public void updateUserQuoteRecords(int userId, int quoteId) {
        List<Record> records = recordMapper.queryUserQuoteRecords(userId, quoteId);
        int counter = records.size() - 10;
        for (Record record : records) {
            if (counter < 0) {
                break;
            }
            recordMapper.deleteRecord(record.getId());
            counter--;
        }
    }
}
