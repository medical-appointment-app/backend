package medical.app.backend.catalog.dto;

import medical.app.backend.catalog.model.CatalogItem;

import java.math.BigDecimal;

public record CatalogItemResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        boolean available) {

    public static CatalogItemResponse from(CatalogItem item) {
        return new CatalogItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory(),
                item.isAvailable()
        );
    }
}
