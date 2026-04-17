package medical.app.backend.common.model;

import medical.app.backend.common.ui.UiElement;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Uniform return type for every "execute" use case (anything that concludes a
 * transaction — booking, cancelling, reserving, confirming, etc.).
 * <p>
 * A {@code TransactionResult} fully describes what the client should render
 * next: a status-aware message, the transaction payload (if any), and a list
 * of {@link UiElement}s ({@code TEXT} blocks, buttons, …) that make up the
 * result screen. Controllers convert it to {@link ApiResponse} via
 * {@code ResponseBuilder.transactional(...)}.
 */
public record TransactionResult<T>(
        boolean success,
        HttpStatus status,
        String message,
        T data,
        List<UiElement> elements) {

    // ── Success factories ────────────────────────────────────────────────────

    public static <T> TransactionResult<T> success(String message, T data, List<UiElement> elements) {
        return new TransactionResult<>(true, HttpStatus.OK, message, data, elements);
    }

    public static <T> TransactionResult<T> created(String message, T data, List<UiElement> elements) {
        return new TransactionResult<>(true, HttpStatus.CREATED, message, data, elements);
    }

    // ── Failure factories ────────────────────────────────────────────────────

    public static <T> TransactionResult<T> failure(HttpStatus status, String message, List<UiElement> elements) {
        return new TransactionResult<>(false, status, message, null, elements);
    }
}
