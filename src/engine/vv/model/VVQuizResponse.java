package engine.vv.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class VVQuizResponse {
    private boolean success;
    private String feedback;
}
