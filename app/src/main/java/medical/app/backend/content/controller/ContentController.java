package medical.app.backend.content.controller;

import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.common.web.ResponseBuilder;
import medical.app.backend.content.dto.ContentByLocaleRequest;
import medical.app.backend.content.dto.ContentBySlugRequest;
import medical.app.backend.content.dto.ContentPageResponse;
import medical.app.backend.content.usecase.GetAllContentPagesUseCase;
import medical.app.backend.content.usecase.GetContentPageUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final GetContentPageUseCase getContentPageUseCase;
    private final GetAllContentPagesUseCase getAllContentPagesUseCase;

    public ContentController(
            GetContentPageUseCase getContentPageUseCase,
            GetAllContentPagesUseCase getAllContentPagesUseCase) {
        this.getContentPageUseCase = getContentPageUseCase;
        this.getAllContentPagesUseCase = getAllContentPagesUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContentPageResponse>>> getAll(
            @RequestParam(defaultValue = "en") String locale) {
        return ResponseBuilder.ok(getAllContentPagesUseCase.execute(new ContentByLocaleRequest(locale)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ContentPageResponse>> getBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "en") String locale) {
        return ResponseBuilder.ok(getContentPageUseCase.execute(new ContentBySlugRequest(slug, locale)));
    }
}
