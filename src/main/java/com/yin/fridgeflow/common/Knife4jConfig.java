//package com.yin.fridgeflow.common;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
///**
// * Knife4j 配置类
// */
//@Configuration
//public class Knife4jConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("项目接口文档")
//                        .version("1.0.0")
//                        .description("Spring Boot 3.x 整合 Knife4j 示例")
//                        .contact(new Contact()
//                                .name("开发者姓名")
//                                .email("dev@example.com"))
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
//    }
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("default")
//                .packagesToScan("com.example.controller")  // 改成你的包路径
//                .build();
//    }
//}
