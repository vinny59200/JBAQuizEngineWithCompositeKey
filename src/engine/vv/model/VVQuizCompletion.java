package engine.vv.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
@Builder(toBuilder = true)
public class VVQuizCompletion {
    private Integer id;
    private LocalDateTime completedAt;

    public static VVQuizCompletion mapFromVVQuiz(VVQuiz quiz) {
        VVQuizCompletion vvQuizCompletion = new VVQuizCompletion();
        vvQuizCompletion.id = quiz.getId().getQuizId()==null?0:quiz.getId().getQuizId();
        vvQuizCompletion.completedAt = quiz.getId().getQuizDate();
//        log.info("VV53 Mapped VVQuizCompletion: {} {}", vvQuizCompletion, quiz.getOwner());
        return vvQuizCompletion;
    }
}