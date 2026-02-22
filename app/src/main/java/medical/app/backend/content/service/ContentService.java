package medical.app.backend.content.service;

import medical.app.backend.content.dto.ContentByLocaleRequest;
import medical.app.backend.content.dto.ContentBySlugRequest;
import medical.app.backend.content.dto.ContentPageResponse;

import java.util.List;

public interface ContentService {

    ContentPageResponse getBySlug(ContentBySlugRequest request);

    List<ContentPageResponse> getAllByLocale(ContentByLocaleRequest request);
}
