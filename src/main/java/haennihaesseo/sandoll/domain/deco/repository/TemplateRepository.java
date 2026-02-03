package haennihaesseo.sandoll.domain.deco.repository;

import haennihaesseo.sandoll.domain.deco.entity.Template;
import haennihaesseo.sandoll.domain.deco.entity.enums.Size;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Template findByNameAndSize(String name, Size size);

    List<Template> findByNameInAndSize(List<String> names, Size size);
}
