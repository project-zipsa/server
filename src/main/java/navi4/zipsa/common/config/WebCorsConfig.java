package navi4.zipsa.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    private static final String ALLOWED_ORIGIN = "https://web-zipsa-client-m04vkeuc49c11c0a.sel4.cloudtype.app";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(ALLOWED_ORIGIN) // 허용할 Origin
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true) // 자격 증명(쿠키 등) 허용
                .maxAge(3600); // Preflight 요청 캐시 시간(초)
    }
}
