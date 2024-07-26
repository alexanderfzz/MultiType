package grooop.mapper;

import grooop.pojo.Quote;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuoteMapper {

    List<Quote> queryAllQuotes();
    Quote queryQuoteById(int id);
    void addQuote(Quote quote);
    void updateQuote(Quote quote);
    void deleteQuote(int id);

    int queryTableSize();

}
