package engine.vv.model;

import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class VVAnswerRequest {
    private List<Integer> answer;
}
