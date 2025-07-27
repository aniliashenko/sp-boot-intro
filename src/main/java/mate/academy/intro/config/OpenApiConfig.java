package mate.academy.intro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .openapi("3.0.1")
                .info(new Info()
                        .title("Book Store API")
                        .version("1.0")
                        .description("API for managing books"));
    }
}
