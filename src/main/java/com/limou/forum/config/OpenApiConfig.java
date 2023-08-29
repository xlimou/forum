package com.limou.forum.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口文档配置
 * @author 小李哞哞
 * @date 2023/8/28
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("哞哞论坛系统API")
                        .description("哞哞论坛系统前后端分离API测试")
                        .contact(new Contact()
                                .name("小李哞哞")
                                .email("1330622668@qq.com")
                                .url("http://limou.love"))
                        .version("0.0.1"));
    }
}
