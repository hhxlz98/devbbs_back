package com.scut.devbbs;

import com.scut.devbbs.constants.ParameterConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.scut.devbbs.dao")
@EnableConfigurationProperties({ParameterConstant.class})
public class DevbbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevbbsApplication.class, args);
    }

}
