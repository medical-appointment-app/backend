package medical.app.backend.common.ui;

/**
 * What should happen when the user activates a button.
 * Only applies to elements with {@link UiElementType#BUTTON}.
 */
public enum ButtonActionType {
    /** Go back one step in the client's navigation history. */
    BACK,
    /** Navigate to the home page. */
    HOME,
    /** Navigate to a specific page — carried by {@code NavigateButton.pageId}. */
    NAVIGATE,
}
