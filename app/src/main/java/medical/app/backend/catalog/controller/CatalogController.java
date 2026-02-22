package medical.app.backend.catalog.controller;

import medical.app.backend.catalog.dto.CatalogItemResponse;
import medical.app.backend.catalog.service.CatalogService;
import medical.app.backend.common.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CatalogItemResponse>>> getAll(
            @RequestParam(required = false) String category) {
        List<CatalogItemResponse> items = (category != null)
                ? catalogService.getByCategory(category)
                : catalogService.getAvailable();
        return ResponseEntity.ok(ApiResponse.ok(items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CatalogItemResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(catalogService.getById(id)));
    }
}
