package medical.app.backend.catalog.service;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.dto.CatalogItemsQuery;
import medical.app.backend.catalog.repository.CatalogItemRepository;
import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.common.model.PagedResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    private final CatalogItemRepository catalogItemRepository;

    public CatalogServiceImpl(CatalogItemRepository catalogItemRepository) {
        this.catalogItemRepository = catalogItemRepository;
    }

    @Override
    public PagedResponse<CatalogItemResponse> getItems(CatalogItemsQuery query) {
        var pageable = PageRequest.of(query.page(), query.size(), Sort.by("name").ascending());

        var page = query.category() != null
                ? catalogItemRepository.findByCategoryAndAvailableTrue(query.category(), pageable)
                : catalogItemRepository.findByAvailableTrue(pageable);

        return PagedResponse.from(page, CatalogItemResponse::from);
    }

    @Override
    public CatalogItemResponse getById(Long id) {
        return catalogItemRepository.findById(id)
                .map(CatalogItemResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("CatalogItem", id));
    }
}
