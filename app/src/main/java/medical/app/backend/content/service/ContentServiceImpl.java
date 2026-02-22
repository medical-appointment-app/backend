package medical.app.backend.content.service;

import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.content.dto.ContentByLocaleRequest;
import medical.app.backend.content.dto.ContentBySlugRequest;
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
    public ContentPageResponse getBySlug(ContentBySlugRequest request) {
        return contentPageRepository.findBySlugAndLocale(request.slug(), request.locale())
                .map(ContentPageResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Content page not found: slug=" + request.slug() + ", locale=" + request.locale()));
    }

    @Override
    public List<ContentPageResponse> getAllByLocale(ContentByLocaleRequest request) {
        return contentPageRepository.findByLocale(request.locale())
                .stream()
                .map(ContentPageResponse::from)
                .toList();
    }
}
