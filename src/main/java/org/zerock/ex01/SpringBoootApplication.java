package org.zerock.ex01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBoootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoootApplication.class, args);
    }
}
