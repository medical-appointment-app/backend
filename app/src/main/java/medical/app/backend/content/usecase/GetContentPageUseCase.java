package medical.app.backend.content.usecase;

import medical.app.backend.content.dto.ContentBySlugRequest;
import medical.app.backend.content.dto.ContentPageResponse;
import medical.app.backend.content.service.ContentService;
import org.springframework.stereotype.Component;

@Component
public class GetContentPageUseCase {

    private final ContentService contentService;

    public GetContentPageUseCase(ContentService contentService) {
        this.contentService = contentService;
    }

    public ContentPageResponse execute(ContentBySlugRequest request) {
        return contentService.getBySlug(request);
    }
}
