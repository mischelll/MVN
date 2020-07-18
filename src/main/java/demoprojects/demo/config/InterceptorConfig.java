package demoprojects.demo.config;

import demoprojects.demo.web.interceptors.FaviconInterceptor;
import demoprojects.demo.web.interceptors.PageTitleInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final FaviconInterceptor faviconInterceptor;
    private final PageTitleInterceptor titleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(faviconInterceptor);
        registry.addInterceptor(titleInterceptor);
    }


}
