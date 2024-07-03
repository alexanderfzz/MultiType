package grooop.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quote {
    private Integer id;
    private String content;
    private Integer length;
    private String source;
}
