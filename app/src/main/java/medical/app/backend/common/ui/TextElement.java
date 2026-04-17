package medical.app.backend.common.ui;

import lombok.Getter;

/**
 * A block of informational text with a heading.
 * <p>
 * The base class's {@code label} carries the body text; {@code title} carries the heading
 * shown above it. Use {@code null} for {@code title} when the text should be rendered
 * without a heading.
 */
@Getter
public class TextElement extends UiElement {

    private final String title;

    public TextElement(String title, String body) {
        super(UiElementType.TEXT, body);
        this.title = title;
    }
}
