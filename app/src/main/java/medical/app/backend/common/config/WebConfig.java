package medical.app.backend.common.config;

import medical.app.backend.session.SessionValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SessionValidationInterceptor sessionValidationInterceptor;

    public WebConfig(SessionValidationInterceptor sessionValidationInterceptor) {
        this.sessionValidationInterceptor = sessionValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionValidationInterceptor)
                .addPathPatterns("/api/**")
                // Public paths that do not require an authenticated session
                .excludePathPatterns(
                        "/api/catalog/**",
                        "/api/content/**",
                        "/api/doctor/**"
                );
    }
}
