package grooop.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    private Integer id;
    private Integer userId;
    private Integer quoteId;
    private Double time;
    private Double wpm;
    private Date date;
}


