package medical.app.backend.common.usecase;

import medical.app.backend.common.exception.BusinessException;
import medical.app.backend.common.exception.ResourceNotFoundException;
import medical.app.backend.common.exception.UnauthorizedException;
import medical.app.backend.common.model.TransactionResult;
import medical.app.backend.common.ui.UiElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * <p>Failure classification:
 * <ul>
 *   <li><b>{@link BusinessException}</b> — forwarded verbatim (its status,
 *       message, and attached elements). Use this for explicit, user-facing
 *       business failures.</li>
 *   <li><b>{@link IllegalStateException}</b> — treated as a 409 with the
 *       exception's own message (temporary: callers should migrate to
 *       {@code BusinessException}).</li>
 *   <li><b>{@link ResourceNotFoundException}</b> / <b>{@link UnauthorizedException}</b>
 *       — mapped to 404 / 401 with their own message.</li>
 *   <li><b>{@link DataAccessException}</b> / <b>{@link TransactionException}</b>
 *       — DB / transaction failures. 500 with the use-case-specific message
 *       from {@link #technicalFailureMessage()}.</li>
 *   <li><b>Any other {@link Exception}</b> — 500 with the vague
 *       {@code UNKNOWN_FAILURE_MESSAGE}. The original exception is logged.</li>
 * </ul>
 */
public abstract class ExecuteUseCase {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String UNKNOWN_FAILURE_MESSAGE =
            "Something went wrong. Please try again in a moment.";

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
            // Meaningful category, but not a meaningful *message* — use the canned one.
            log.error("Technical failure in {}", getClass().getSimpleName(), e);
            return TransactionResult.failure(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    technicalFailureMessage(),
                    defaultFailureElements());

        } catch (Exception e) {
            log.error("Unexpected failure in {}", getClass().getSimpleName(), e);
            return TransactionResult.failure(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    UNKNOWN_FAILURE_MESSAGE,
                    defaultFailureElements());
        }
    }

    /**
     * Message shown when a technical (DB / transaction) failure occurs.
     * Subclasses override to be specific — e.g. "We couldn't complete your booking.".
     */
    protected abstract String technicalFailureMessage();

    /**
     * UI elements attached to failure results when the exception itself didn't carry any.
     * Default: Back + Home buttons. Override to change.
     */
    protected List<UiElement> defaultFailureElements() {
        return List.of(UiElement.backButton(), UiElement.homeButton());
    }
}
