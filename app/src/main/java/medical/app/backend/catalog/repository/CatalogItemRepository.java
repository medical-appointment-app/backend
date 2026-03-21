package medical.app.backend.catalog.repository;

import medical.app.backend.catalog.model.CatalogItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {

    Page<CatalogItem> findByAvailableTrue(Pageable pageable);

    Page<CatalogItem> findByCategoryAndAvailableTrue(String category, Pageable pageable);
}
