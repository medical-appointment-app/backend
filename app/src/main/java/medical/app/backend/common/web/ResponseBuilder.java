package medical.app.backend.common.web;

import medical.app.backend.common.model.ApiResponse;
import medical.app.backend.common.model.TransactionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Central factory for building HTTP responses with a consistent ApiResponse envelope.
 * Controllers should use only these methods instead of constructing ResponseEntity inline.
 */
public final class ResponseBuilder {

    private ResponseBuilder() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.ok(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    public static ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.noContent().build();
    }

    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiResponse.error(message));
    }

    /**
     * Convert a {@link TransactionResult} (returned by any execute-type use case)
     * into the standard HTTP envelope. Status, message, data, and elements are all
     * forwarded unchanged.
     */
    public static <T> ResponseEntity<ApiResponse<T>> transactional(TransactionResult<T> result) {
        ApiResponse<T> body = new ApiResponse<>(
                result.success(),
                result.message(),
                result.data(),
                result.elements());
        return ResponseEntity.status(result.status()).body(body);
    }
}
