package medical.app.backend.catalog.repository;

import medical.app.backend.catalog.model.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {

    List<CatalogItem> findByAvailableTrue();

    List<CatalogItem> findByCategoryAndAvailableTrue(String category);
}
