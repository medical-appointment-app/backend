package medical.app.backend.common.ui;

/**
 * Discriminator for {@link UiElement} — tells the client what to render.
 * Add new values here (and, if they need extra fields, a dedicated
 * {@link UiElement} subclass) whenever the UI gains a new component.
 */
public enum UiElementType {
    BUTTON,
    TEXT,
}
