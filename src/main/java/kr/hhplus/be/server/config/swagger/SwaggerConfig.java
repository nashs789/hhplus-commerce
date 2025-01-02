package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("HHPlus E-Commerce API")
                                            .description("API Documentation for E-Commerce API")
                                            .version("v1.0"));
    }
}
