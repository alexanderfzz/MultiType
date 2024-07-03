package grooop.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UtilMapper {
    void resetUserAutoIncrement(int value);
    void resetQuoteAutoIncrement(int value);
    void resetRecordAutoIncrement(int value);
}
