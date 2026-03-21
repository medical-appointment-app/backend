package medical.app.backend.catalog.usecase;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.dto.CatalogItemsQuery;
import medical.app.backend.catalog.service.CatalogService;
import medical.app.backend.common.model.PagedResponse;
import org.springframework.stereotype.Component;

@Component
public class GetCatalogItemsUseCase {

    private final CatalogService catalogService;

    public GetCatalogItemsUseCase(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public PagedResponse<CatalogItemResponse> execute(CatalogItemsQuery query) {
        return catalogService.getItems(query);
    }
}
