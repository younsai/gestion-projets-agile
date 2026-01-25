package io.younsai.gestionprojetsagile.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestion Projets Agile API")
                        .version("1.0.0")
                        .description("API pour gérer ProductBacklogs, UserStories, Epics, Sprints et Tasks")
                        .contact(new Contact().name("Younsai").email("younsai@example.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                );
    }
}

