package mate.academy.intro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public static final String OPEN_API_VERSION = "3.0.1";
    public static final String INFO_TITLE = "Book Store API";
    public static final String INFO_VERSION = "1.0";
    public static final String INFO_DESCRIPTION = "API for managing books";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .openapi(OPEN_API_VERSION)
                .info(new Info()
                        .title(INFO_TITLE)
                        .version(INFO_VERSION)
                        .description(INFO_DESCRIPTION));
    }
}
