package com.ted.resonance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * swagger2文档相关配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ted.resonance.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())  //apiInfo 可用于接口简介
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        String description = "resonance 共振 knights 骑士团 account 账户\n " +
                "国际化的请求头添加参数 language, en-us 英文, KRW 韩文, 其他或不传默认中文 ";
        return new ApiInfo(
                "TED钱包接口文档",
                description,
                "API TOS",
                null,
                new Contact("He Goudai", "www.hegoudai.com", "2457431994@qq.com"),
                null, null, Collections.emptyList());
    }

}
