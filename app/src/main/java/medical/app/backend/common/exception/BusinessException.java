package medical.app.backend.common.exception;

import lombok.Getter;
import medical.app.backend.common.ui.UiElement;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thrown anywhere in the app when a business rule fails and the client needs to
 * render an error screen described by a list of {@link UiElement}s (buttons today,
 * any page element tomorrow).
 * <p>
 * The HTTP status is a required parameter so each call-site explicitly decides
 * whether the failure is a client error (4xx) or a server/infrastructure error (5xx).
 * <p>
 * Use the fluent {@link Builder} API to attach elements:
 * <pre>{@code
 *   throw BusinessException.of("This slot is already taken.", HttpStatus.CONFLICT)
 *           .withBackButton()
 *           .withHomeButton()
 *           .withNavigateButton("Find another slot", "appointments")
 *           .build();
 * }</pre>
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final List<UiElement> elements;

    private BusinessException(String message, HttpStatus status, List<UiElement> elements) {
        super(message);
        this.status = status;
        this.elements = Collections.unmodifiableList(elements);
    }

    // ── Fluent builder ────────────────────────────────────────────────────────

    /**
     * Start building a {@link BusinessException}.
     *
     * @param message user-facing error message
     * @param status  HTTP status to return — pick a 4xx for client errors
     *                (e.g. {@link HttpStatus#CONFLICT}, {@link HttpStatus#BAD_REQUEST})
     *                or a 5xx for server/infrastructure failures
     *                (e.g. {@link HttpStatus#INTERNAL_SERVER_ERROR},
     *                {@link HttpStatus#SERVICE_UNAVAILABLE}).
     */
    public static Builder of(String message, HttpStatus status) {
        return new Builder(message, status);
    }

    public static final class Builder {
        private final String message;
        private final HttpStatus status;
        private final List<UiElement> elements = new ArrayList<>();

        private Builder(String message, HttpStatus status) {
            this.message = message;
            this.status = status;
        }

        public Builder withBackButton() {
            elements.add(UiElement.backButton());
            return this;
        }

        public Builder withHomeButton() {
            elements.add(UiElement.homeButton());
            return this;
        }

        public Builder withNavigateButton(String label, String pageId) {
            elements.add(UiElement.navigateButton(label, pageId));
            return this;
        }

        public Builder withText(String title, String body) {
            elements.add(UiElement.text(title, body));
            return this;
        }

        public Builder withText(String body) {
            elements.add(UiElement.text(body));
            return this;
        }

        /** Attach any {@link UiElement} — use this for subclasses not covered by the shortcuts above. */
        public Builder withElement(UiElement element) {
            elements.add(element);
            return this;
        }

        public BusinessException build() {
            return new BusinessException(message, status, elements);
        }
    }
}
