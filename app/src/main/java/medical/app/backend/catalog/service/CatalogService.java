package medical.app.backend.catalog.service;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.dto.CatalogItemsQuery;
import medical.app.backend.common.model.PagedResponse;

public interface CatalogService {

    PagedResponse<CatalogItemResponse> getItems(CatalogItemsQuery query);

    CatalogItemResponse getById(Long id);
}
