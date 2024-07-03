package grooop;

import grooop.mapper.UserMapper;
import grooop.mapper.UtilMapper;
import grooop.mapper.RecordMapper;
import grooop.pojo.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

@SpringBootTest
class TypeTestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UtilMapper utilMapper;

    @Test
    void contextLoads() {
        utilMapper.resetRecordAutoIncrement(0);
    }

}
