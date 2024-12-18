package com.project.springsecuritypractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // jpa 활성화 어노테이션
public class JpaAuditorConfig {
}
