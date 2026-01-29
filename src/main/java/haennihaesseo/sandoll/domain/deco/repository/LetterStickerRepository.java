package haennihaesseo.sandoll.domain.deco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterStickerRepository extends JpaRepository<LetterSticker, Long> {
}
