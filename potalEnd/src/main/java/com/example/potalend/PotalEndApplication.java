package com.example.potalend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Date 어쩌구 활용하려면 써야함.
@SpringBootApplication
public class PotalEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(PotalEndApplication.class, args);
    }

}
