package medical.app.backend.content.dto;

import medical.app.backend.content.model.ContentPage;

public record ContentPageResponse(
        Long id,
        String slug,
        String title,
        String body,
        String locale) {

    public static ContentPageResponse from(ContentPage page) {
        return new ContentPageResponse(
                page.getId(),
                page.getSlug(),
                page.getTitle(),
                page.getBody(),
                page.getLocale()
        );
    }
}
