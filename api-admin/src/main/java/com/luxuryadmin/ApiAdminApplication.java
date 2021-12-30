package com.luxuryadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 商家pc端后台
 *
 * @author Administrator
 */
@SpringBootApplication
@ServletComponentScan
public class ApiAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAdminApplication.class, args);
    }

}
