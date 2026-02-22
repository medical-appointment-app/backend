package medical.app.backend.catalog.usecase;

import medical.app.backend.catalog.dto.CatalogByCategoryRequest;
import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.service.CatalogService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetCatalogItemsUseCase {

    private final CatalogService catalogService;

    public GetCatalogItemsUseCase(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public List<CatalogItemResponse> execute(String category) {
        if (category != null) {
            return catalogService.getByCategory(new CatalogByCategoryRequest(category));
        }
        return catalogService.getAvailable();
    }
}
