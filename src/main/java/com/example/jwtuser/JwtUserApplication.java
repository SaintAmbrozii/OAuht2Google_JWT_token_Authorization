package com.example.jwtuser;

import com.example.jwtuser.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({AppProperties.class})
@SpringBootApplication
public class JwtUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtUserApplication.class, args);
    }

}
