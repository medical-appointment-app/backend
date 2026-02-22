package medical.app.backend.content.service;

import medical.app.backend.content.dto.ContentPageResponse;

import java.util.List;

public interface ContentService {

    ContentPageResponse getBySlug(String slug, String locale);

    List<ContentPageResponse> getAllByLocale(String locale);
}
