package com.devhub.pinchesystemback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableSwagger2
public class PincheSystemBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(PincheSystemBackApplication.class, args);
    }

}
