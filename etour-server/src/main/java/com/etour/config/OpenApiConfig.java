package com.etour.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI配置类
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ETour API文档")
                        .description("AI智能旅游攻略系统API文档")
                        .contact(new Contact()
                                .name("etour")
                                .url("https://etour.com")
                                .email("2718336395@etour.com"))
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }
}