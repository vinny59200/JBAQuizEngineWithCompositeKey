package engine.vv.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder(toBuilder = true)
public class VVQuiz {

    @EmbeddedId
    private VVQuizId id;

    @Column
    @NotNull
    private String title;
    @Column
    @NotNull
    private String text;
    @Column
    @NotNull
    @Size(min = 2)
    @Convert(converter = StringListConverter.class)
    private List<String> options;
    @Column
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> answer;
    @Column
    private String owner;
    @Column(columnDefinition = "boolean default false")
    private boolean solved;
    @Column
    private String player;

}
