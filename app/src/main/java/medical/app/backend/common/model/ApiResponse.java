package medical.app.backend.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import medical.app.backend.common.ui.UiElement;

import java.util.List;

/**
 * Standard envelope returned by every REST endpoint. {@code elements} is only populated
 * when the response carries renderable page elements for the client (typically errors).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        List<UiElement> elements) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, null, data, null);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null, null);
    }

    public static ApiResponse<Void> error(String message, List<UiElement> elements) {
        return new ApiResponse<>(false, message, null, elements);
    }
}
