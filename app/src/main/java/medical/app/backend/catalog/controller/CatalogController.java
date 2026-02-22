package medical.app.backend.catalog.controller;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.usecase.GetCatalogItemByIdUseCase;
import medical.app.backend.catalog.usecase.GetCatalogItemsUseCase;
import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.common.web.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse<List<CatalogItemResponse>>> getAll(
            @RequestParam(required = false) String category) {
        return ResponseBuilder.ok(getCatalogItemsUseCase.execute(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CatalogItemResponse>> getById(@PathVariable Long id) {
        return ResponseBuilder.ok(getCatalogItemByIdUseCase.execute(id));
    }
}
