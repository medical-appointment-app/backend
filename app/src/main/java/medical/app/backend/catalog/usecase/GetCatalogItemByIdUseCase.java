package medical.app.backend.catalog.usecase;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.service.CatalogService;
import org.springframework.stereotype.Component;

@Component
public class GetCatalogItemByIdUseCase {

    private final CatalogService catalogService;

    public GetCatalogItemByIdUseCase(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public CatalogItemResponse execute(Long id) {
        return catalogService.getById(id);
    }
}
