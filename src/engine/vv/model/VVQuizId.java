package engine.vv.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@Embeddable
public class VVQuizId implements java.io.Serializable {
    private Integer quizId;
    private LocalDateTime quizDate;
}
