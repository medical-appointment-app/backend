package medical.app.backend.common.ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * Base model for any element the backend asks the client to render.
 * <p>
 * Use this class directly for simple elements that need nothing beyond a
 * {@link UiElementType} and a display {@code label} (e.g. a Back or Home button).
 * Subclass it when extra fields are required — see {@link NavigateButton} —
 * and Jackson will include those fields automatically when serialising.
 * <p>
 * Returned as a list so a single response can describe an entire screen:
 * the client iterates the list and renders each element based on its {@code type}.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UiElement {

    private final UiElementType type;
    private final String label;

    public UiElement(UiElementType type, String label) {
        this.type = type;
        this.label = label;
    }

    // ── Convenience factories ────────────────────────────────────────────────

    public static ButtonElement backButton() {
        return new ButtonElement(ButtonActionType.BACK, "Go back");
    }

    public static ButtonElement backButton(String label) {
        return new ButtonElement(ButtonActionType.BACK, label);
    }

    public static ButtonElement homeButton() {
        return new ButtonElement(ButtonActionType.HOME, "Return to home");
    }

    public static ButtonElement homeButton(String label) {
        return new ButtonElement(ButtonActionType.HOME, label);
    }

    public static NavigateButton navigateButton(String label, String pageId) {
        return new NavigateButton(label, pageId);
    }

    public static TextElement text(String title, String body) {
        return new TextElement(title, body);
    }

    public static TextElement text(String body) {
        return new TextElement(null, body);
    }
}
