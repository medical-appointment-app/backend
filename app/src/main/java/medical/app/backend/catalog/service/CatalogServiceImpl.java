package medical.app.backend.catalog.service;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.repository.CatalogItemRepository;
import medical.app.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    private final CatalogItemRepository catalogItemRepository;

    public CatalogServiceImpl(CatalogItemRepository catalogItemRepository) {
        this.catalogItemRepository = catalogItemRepository;
    }

    @Override
    public List<CatalogItemResponse> getAvailable() {
        return catalogItemRepository.findByAvailableTrue()
                .stream()
                .map(CatalogItemResponse::from)
                .toList();
    }

    @Override
    public List<CatalogItemResponse> getByCategory(String category) {
        return catalogItemRepository.findByCategoryAndAvailableTrue(category)
                .stream()
                .map(CatalogItemResponse::from)
                .toList();
    }

    @Override
    public CatalogItemResponse getById(Long id) {
        return catalogItemRepository.findById(id)
                .map(CatalogItemResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("CatalogItem", id));
    }
}
