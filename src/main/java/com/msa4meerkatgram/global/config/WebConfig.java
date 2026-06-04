package com.msa4meerkatgram.global.config;

import com.msa4meerkatgram.global.util.file.fileConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final fileConfig fileConfig;

    // 파일이 저장된 물리경로 가져오기
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String resourceLocation = Paths.get(fileConfig.storagePath() + "/files").toUri().toString();

        registry.addResourceHandler("files/**").addResourceLocations(resourceLocation);
    }
}
