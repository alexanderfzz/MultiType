package grooop.controller;

import grooop.pojo.Quote;
import grooop.service.impl.QuoteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log/quote")
public class QuoteController {

    @Autowired
    private QuoteServiceImpl quoteService;


    // GET
    @GetMapping("")
    public ResponseEntity<List<Quote>> queryAllQuotes() {
        List<Quote> quotes = quoteService.queryAllQuotes();
        if (quotes != null) {
            return ResponseEntity.ok(quotes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quote> getQuoteById(@PathVariable("id") int id) {
        Quote quote = quoteService.queryQuoteById(id);
        if (quote != null) {
            return ResponseEntity.ok(quote);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    // POST
    @PostMapping("/{id}")
    public ResponseEntity<Quote> updateQuote(@RequestBody Quote quote, @RequestHeader Map<String, String> headers, @PathVariable int id) {
        Quote updatedQuote = quoteService.updateQuote(quote, headers, id);
        if (updatedQuote != null) {
            return ResponseEntity.ok(updatedQuote);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // PUT
    @PutMapping("")
    public ResponseEntity<Quote> addQuote(@RequestBody Quote quote, @RequestHeader Map<String, String> headers) {
        Quote newQuote = quoteService.addQuote(quote, headers);
        if (newQuote != null) {
            return ResponseEntity.ok(quote);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuoteById(@PathVariable("id") int id, @RequestHeader Map<String, String> headers) {
        boolean operationSucceeded = quoteService.deleteQuote(id, headers);
        if (operationSucceeded) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
