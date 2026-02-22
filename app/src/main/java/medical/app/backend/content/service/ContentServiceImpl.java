package medical.app.backend.content.service;

import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.content.dto.ContentPageResponse;
import medical.app.backend.content.repository.ContentPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

    private final ContentPageRepository contentPageRepository;

    public ContentServiceImpl(ContentPageRepository contentPageRepository) {
        this.contentPageRepository = contentPageRepository;
    }

    @Override
    public ContentPageResponse getBySlug(String slug, String locale) {
        return contentPageRepository.findBySlugAndLocale(slug, locale)
                .map(ContentPageResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Content page not found: slug=" + slug + ", locale=" + locale));
    }

    @Override
    public List<ContentPageResponse> getAllByLocale(String locale) {
        return contentPageRepository.findByLocale(locale)
                .stream()
                .map(ContentPageResponse::from)
                .toList();
    }
}
