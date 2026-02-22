package medical.app.backend.content.usecase;

import medical.app.backend.content.dto.ContentByLocaleRequest;
import medical.app.backend.content.dto.ContentPageResponse;
import medical.app.backend.content.service.ContentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllContentPagesUseCase {

    private final ContentService contentService;

    public GetAllContentPagesUseCase(ContentService contentService) {
        this.contentService = contentService;
    }

    public List<ContentPageResponse> execute(ContentByLocaleRequest request) {
        return contentService.getAllByLocale(request);
    }
}
