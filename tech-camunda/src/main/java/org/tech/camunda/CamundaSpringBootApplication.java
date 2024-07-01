package org.tech.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProcessApplication
@SpringBootApplication
public class CamundaSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(CamundaSpringBootApplication.class, args);
    }
}