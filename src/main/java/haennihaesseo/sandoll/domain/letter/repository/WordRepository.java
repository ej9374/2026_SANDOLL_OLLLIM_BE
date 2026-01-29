package haennihaesseo.sandoll.domain.letter.repository;

import haennihaesseo.sandoll.domain.letter.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("select w from Word w where w.letter.letterId = :letterId order by w.wordOrder asc")
    List<Word> findByLetterLetterIdOrderByWordOrderAsc(@Param("letterId") Long letterId);
}
