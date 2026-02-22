package medical.app.backend.content.controller;

import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.content.dto.ContentPageResponse;
import medical.app.backend.content.service.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContentPageResponse>>> getAll(
            @RequestParam(defaultValue = "en") String locale) {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getAllByLocale(locale)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ContentPageResponse>> getBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "en") String locale) {
        return ResponseEntity.ok(ApiResponse.ok(contentService.getBySlug(slug, locale)));
    }
}
