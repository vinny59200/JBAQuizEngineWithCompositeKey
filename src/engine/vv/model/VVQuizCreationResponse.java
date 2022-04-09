package engine.vv.model;

import lombok.*;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
public class VVQuizCreationResponse {
    private Integer id;
    private String title;
    private String text;
    private List<String> options;

    public static VVQuizCreationResponse mapFromVVQuiz(VVQuiz quiz) {
        VVQuizCreationResponse quizCreationResponse = new VVQuizCreationResponse();
        quizCreationResponse.id = quiz.getId().getQuizId()==null?0:quiz.getId().getQuizId();
        quizCreationResponse.title = quiz.getTitle();
        quizCreationResponse.text = quiz.getText();
        quizCreationResponse.options = quiz.getOptions();
        return quizCreationResponse;
    }
}
