package medical.app.backend.content.repository;

import medical.app.backend.content.model.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentPageRepository extends JpaRepository<ContentPage, Long> {

    Optional<ContentPage> findBySlugAndLocale(String slug, String locale);

    List<ContentPage> findByLocale(String locale);
}
