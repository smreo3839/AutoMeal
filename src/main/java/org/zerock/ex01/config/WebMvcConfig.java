package org.zerock.ex01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/getImages/**")
                .addResourceLocations("file:src/main/resources/static/upload/");

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //모든 경로에 대해
        registry.addMapping("/**")
                //Origin이 로컬호스트 3000포트에 대해
                //.allowedOrigins("http://127.0.0.1:3000/", "http://localhost:3000/", "https://jackytodo.netlify.app/")
                .allowedOrigins("*")
                //Get,post,put,delete,patch,options 메서드 허용함
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                //.allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
