package medical.app.backend.catalog.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CatalogItemsQuery(
        String category,
        @Min(0) int page,
        @Min(1) @Max(100) int size) {
}
