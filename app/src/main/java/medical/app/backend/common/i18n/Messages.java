package medical.app.backend.common.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Thin facade around Spring's {@link MessageSource} that resolves keys against
 * the locale of the current request.
 * <p>
 * The locale is taken from {@link LocaleContextHolder}, which is populated by
 * {@link medical.app.backend.common.config.LocaleConfig#localeResolver()} from
 * the incoming {@code Accept-Language} header.
 * <p>
 * Usage:
 * <pre>{@code
 *   messages.get("appointment.slot.taken");
 *   messages.get("appointment.book.confirmation.body", scheduledAt);
 * }</pre>
 */
@Component
public class Messages {

    private final MessageSource messageSource;

    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /** Resolve {@code code} for the current request's locale, returning the code itself if not found. */
    public String get(String code, Object... args) {
        return get(code, LocaleContextHolder.getLocale(), args);
    }

    /** Resolve {@code code} for a specific {@link Locale}. */
    public String get(String code, Locale locale, Object... args) {
        // Third arg is the "default message" — returning the code makes missing
        // keys obvious both in responses and in logs so translations are added.
        return messageSource.getMessage(code, args, code, locale);
    }
}
