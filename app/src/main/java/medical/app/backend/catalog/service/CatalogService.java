package medical.app.backend.catalog.service;

import medical.app.backend.catalog.dto.CatalogByCategoryRequest;
import medical.app.backend.catalog.dto.CatalogItemResponse;

import java.util.List;

public interface CatalogService {

    List<CatalogItemResponse> getAvailable();

    List<CatalogItemResponse> getByCategory(CatalogByCategoryRequest request);

    CatalogItemResponse getById(Long id);
}
