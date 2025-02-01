package kr.hhplus.be.server.global.config;

import kr.hhplus.be.server.global.interceptor.MemberInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final MemberInterceptor memberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO - */{memberId} 너무 포괄적임
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns(
                        "/api/v1/*/point/{memberId}",
                        "/api/v1/*/point/{memberId}/charge",
                        "/api/v1/*/point/{memberId}/use",
                        "/api/v1/*/{memberId}/cart",
                        "/api/v1/*/{memberId}/cart/add",
                        "/api/v1/*/member/{memberId}/list",
                        "/api/v1/*/{couponId}/member/{memberId}",
                        "/api/v1/*/{memberId}",
                        "/api/v1/*/member/{memberId}/order/{orderId}"
                )
                .excludePathPatterns(
                        "/api/v1/product/ranking"
                );
    }
}
