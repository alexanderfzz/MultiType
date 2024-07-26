package grooop.service;

import grooop.pojo.Quote;

import java.util.List;
import java.util.Map;

public interface QuoteService {
    List<Quote> queryAllQuotes();
    Quote queryQuoteById(int id);
    Quote addQuote(Quote quote, Map<String, String> headers);
    Quote updateQuote(Quote quote, Map<String, String> headers, int id);
    boolean deleteQuote(int id, Map<String, String> headers);

    int queryTableSize();

}
