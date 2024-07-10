package grooop.service;

import grooop.JwtUtil;
import grooop.mapper.QuoteMapper;
import grooop.pojo.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteMapper quoteMapper;

    @Autowired
    private RecordServiceImpl recordService;


    @Override
    public List<Quote> queryAllQuotes() {
        List<Quote> quotes = quoteMapper.queryAllQuotes();
        return quotes;
    }

    @Override
    public Quote queryQuoteById(int id) {
        Quote quote = quoteMapper.queryQuoteById(id);
        return quote;
    }

    @Override
    public Quote addQuote(Quote quote, Map<String, String> headers) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            quote.setLength(quote.getContent().length());
            quoteMapper.addQuote(quote);
            return quote;
        } else {
            return null;
        }
    }

    @Override
    public Quote updateQuote(Quote quote, Map<String, String> headers, int id) {
        String token = headers.get("authorization");
        if (JwtUtil.validateToken(token)) {
            quote.setLength(quote.getContent().length());
            System.out.println(quote);
            quoteMapper.updateQuote(quote);
            return quote;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteQuote(int id, Map<String, String> headers) {
        String token = headers.get("authorization");
        boolean validated = JwtUtil.validateToken(token);
        if (validated) {
            recordService.deleteQuoteRecords(id, headers);
            quoteMapper.deleteQuote(id);
        }
        return validated;
    }
}
