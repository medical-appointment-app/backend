package medical.app.backend.common.usecase;

import medical.app.backend.common.exception.BusinessException;
import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.common.exception.UnauthorizedException;
import medical.app.backend.common.i18n.Messages;
import medical.app.backend.common.model.TransactionResult;
import medical.app.backend.common.ui.UiElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.function.Supplier;

/**
 * Base class for every state-changing ("execute") use case.
 * <p>
 * Subclasses write their happy path inside {@link #run(Supplier)} and return a
 * success {@link TransactionResult} from it. This class wraps the body in a
 * try/catch that translates every failure into a failure {@code TransactionResult}
 * with a user-facing message and a set of fallback UI elements, so the client
 * can render the result with a single code path regardless of outcome.
 *
 * <p>User-facing strings are resolved through {@link Messages} so the message
 * language tracks the incoming request's {@code Accept-Language} header.
 */
public abstract class ExecuteUseCase {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /** Message key used when an unexpected (non-technical, non-business) failure occurs. */
    private static final String UNKNOWN_FAILURE_KEY = "error.unknown";

    /** Default key for technical (DB/transaction) failures — use cases should override. */
    protected static final String DEFAULT_TECHNICAL_FAILURE_KEY = "error.unknown";

    @Autowired
    protected Messages messages;

    /**
     * Execute the supplied block and translate failures into a failure
     * {@link TransactionResult}. Subclasses call this from their public
     * {@code execute(...)} methods.
     */
    protected final <T> TransactionResult<T> run(Supplier<TransactionResult<T>> action) {
        try {
            return action.get();

        } catch (BusinessException e) {
            return TransactionResult.failure(e.getStatus(), e.getMessage(), e.getElements());

        } catch (ResourceNotFoundException e) {
            return TransactionResult.failure(HttpStatus.NOT_FOUND, e.getMessage(), defaultFailureElements());

        } catch (UnauthorizedException e) {
            return TransactionResult.failure(HttpStatus.UNAUTHORIZED, e.getMessage(), defaultFailureElements());

        } catch (IllegalStateException e) {
            // Meaningful: the service threw with a user-facing message (e.g. "slot taken").
            return TransactionResult.failure(HttpStatus.CONFLICT, e.getMessage(), defaultFailureElements());

        } catch (DataAccessException | TransactionException e) {
            // Meaningful category, but not a meaningful *message* — use the localized canned one.
            log.error("Technical failure in {}", getClass().getSimpleName(), e);
            return TransactionResult.failure(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messages.get(technicalFailureMessageKey()),
                    defaultFailureElements());

        } catch (Exception e) {
            log.error("Unexpected failure in {}", getClass().getSimpleName(), e);
            return TransactionResult.failure(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messages.get(UNKNOWN_FAILURE_KEY),
                    defaultFailureElements());
        }
    }

    /**
     * Message-bundle key resolved against the request locale when a technical
     * (DB / transaction) failure occurs. Subclasses override to be specific —
     * e.g. {@code "appointment.book.technicalFailure"}.
     */
    protected abstract String technicalFailureMessageKey();

    /**
     * UI elements attached to failure results when the exception itself didn't carry any.
     * Default: Back + Home buttons. Override to change.
     */
    protected List<UiElement> defaultFailureElements() {
        return List.of(UiElement.backButton(), UiElement.homeButton());
    }
}
