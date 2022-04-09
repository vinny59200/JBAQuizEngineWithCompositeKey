package engine.vv.repository;

import engine.vv.model.VVQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PaginatedQuizRepository  extends PagingAndSortingRepository<VVQuiz, Integer> {

    @Query(value="SELECT * FROM VVQUIZ q WHERE (LOWER(q.PLAYER) LIKE LOWER(concat('%', :name,'%'))) AND q.SOLVED= true ORDER BY q.QUIZ_DATE DESC",nativeQuery = true)
    Page<VVQuiz> findByName(@Param("name")String name, Pageable pageable);

    @Query(value="SELECT * FROM VVQUIZ q WHERE q.SOLVED != true",nativeQuery = true)
    Page<VVQuiz> findAllNotSolved( Pageable pageable);
}
