package engine.vv.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class VVQuizCreationRequest {
    private String title;
    private String text;
    private List<String> options;
    private Integer answer;
}
