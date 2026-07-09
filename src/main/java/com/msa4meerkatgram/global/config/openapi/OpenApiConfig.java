package com.msa4meerkatgram.global.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(
                  new Info()
                          .title("Meerkatgram API")  // 문서의 제목
                          .description("Meerkatgram REAT API Document") // 문서 설명
                          .version("v1.0.0") // 문서의 버전
                );
    }
}
