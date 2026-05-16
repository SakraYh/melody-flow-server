package cn.edu.seig.melodyflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MelodyFlow API 文档")
                        .description("在线音乐流媒体平台后端接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MelodyFlow")
                                .email("melodyflow@example.com")));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户端")
                .pathsToMatch("/user/**", "/song/**", "/artist/**", "/playlist/**",
                        "/comment/**", "/favorite/**", "/banner/**", "/feedback/**", "/search/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("管理端")
                .pathsToMatch("/admin/**")
                .build();
    }
}
