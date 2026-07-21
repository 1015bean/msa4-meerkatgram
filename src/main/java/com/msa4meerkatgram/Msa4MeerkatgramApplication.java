package com.msa4meerkatgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
// 어플리케이션 전체에 "JPA Auditing 기능을 사용할 테니 준비해라." 선언
// auditing: 데이터의 생성 및 변경 이력을 자동으로 기록
public class Msa4MeerkatgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(Msa4MeerkatgramApplication.class, args);
    }

}
