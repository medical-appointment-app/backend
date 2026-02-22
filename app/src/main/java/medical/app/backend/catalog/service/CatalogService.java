package medical.app.backend.catalog.service;

import medical.app.backend.catalog.dto.CatalogItemResponse;

import java.util.List;

public interface CatalogService {

    List<CatalogItemResponse> getAvailable();

    List<CatalogItemResponse> getByCategory(String category);

    CatalogItemResponse getById(Long id);
}
