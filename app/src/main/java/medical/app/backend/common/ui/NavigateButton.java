package medical.app.backend.common.ui;

import lombok.Getter;

/**
 * Button that asks the client to navigate to a specific page.
 * The client maps {@code pageId} to a concrete route.
 */
@Getter
public class NavigateButton extends ButtonElement {

    private final String pageId;

    public NavigateButton(String label, String pageId) {
        super(ButtonActionType.NAVIGATE, label);
        this.pageId = pageId;
    }
}
