package net.coffeemachine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenApi(@Value("${app.doc.title}") String title,
                                     @Value("${app.doc.version}") String version,
                                     @Value("${app.doc.description}") String description) {

                return new OpenAPI().info(new Info()
                                        .title(title)
                                        .version(version)
                                        .description(description));
        }

        @Bean
        public GroupedOpenApi api() {
                return GroupedOpenApi.builder()
                        .group("REST API")
                        .pathsToMatch("/control/**")
                        .build();
        }
}
