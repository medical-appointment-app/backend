package medical.app.backend.catalog.controller;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.dto.CatalogItemsQuery;
import medical.app.backend.catalog.usecase.GetCatalogItemByIdUseCase;
import medical.app.backend.catalog.usecase.GetCatalogItemsUseCase;
import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.common.model.PagedResponse;
import medical.app.backend.common.web.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final GetCatalogItemsUseCase getCatalogItemsUseCase;
    private final GetCatalogItemByIdUseCase getCatalogItemByIdUseCase;

    public CatalogController(
            GetCatalogItemsUseCase getCatalogItemsUseCase,
            GetCatalogItemByIdUseCase getCatalogItemByIdUseCase) {
        this.getCatalogItemsUseCase = getCatalogItemsUseCase;
        this.getCatalogItemByIdUseCase = getCatalogItemByIdUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CatalogItemResponse>>> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseBuilder.ok(getCatalogItemsUseCase.execute(new CatalogItemsQuery(category, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CatalogItemResponse>> getById(@PathVariable Long id) {
        return ResponseBuilder.ok(getCatalogItemByIdUseCase.execute(id));
    }
}
