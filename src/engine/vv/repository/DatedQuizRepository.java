package engine.vv.repository;

import engine.vv.model.VVQuiz;
import engine.vv.model.VVQuizId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DatedQuizRepository extends JpaRepository<VVQuiz, VVQuizId> {
    List<VVQuiz> findByIdQuizId(Integer id);

    @Transactional
    void deleteByIdQuizId(Integer id);
}