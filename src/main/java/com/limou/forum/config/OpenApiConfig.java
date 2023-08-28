package com.limou.forum.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 小李哞哞
 * @date 2023/8/28
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("title")
                        .description("desc")
                        .contact(new Contact()
                                .name("name")
                                .email("email")
                                .url("http://limou.love"))
                        .version("0.0.1"));
    }
}
