package medical.app.backend.common.ui;

import lombok.Getter;

/**
 * A clickable button the client should render.
 * <p>
 * The {@link ButtonActionType} tells the client what to do on click. Use this class
 * directly for actions that need no extra data (BACK, HOME); subclass it when extra
 * fields are needed — see {@link NavigateButton}.
 */
@Getter
public class ButtonElement extends UiElement {

    private final ButtonActionType actionType;

    public ButtonElement(ButtonActionType actionType, String label) {
        super(UiElementType.BUTTON, label);
        this.actionType = actionType;
    }
}
