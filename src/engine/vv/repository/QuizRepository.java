package engine.vv.repository;

import engine.vv.model.VVQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<VVQuiz, Integer> {
}
