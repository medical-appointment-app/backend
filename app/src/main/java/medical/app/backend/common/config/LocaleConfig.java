package medical.app.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * Locale configuration for the API.
 * <p>
 * The resolver reads the standard {@code Accept-Language} HTTP header from each
 * incoming request and exposes the negotiated {@link Locale} through
 * {@link org.springframework.context.i18n.LocaleContextHolder LocaleContextHolder}
 * for the duration of the request.
 * <p>
 * Supported locales are {@code tr} (default) and {@code en}. Requests that send
 * no {@code Accept-Language} header, or any unsupported language, fall back to
 * Turkish so messages never come through empty.
 */
@Configuration
public class LocaleConfig {

    static final Locale TURKISH = Locale.forLanguageTag("tr");

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(TURKISH);
        resolver.setSupportedLocales(List.of(TURKISH, Locale.ENGLISH));
        return resolver;
    }
}
