package org.xiaowu.behappy.canal.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class CanalExampleApplication {


    public static void main(String[] args) {

        SpringApplication.run(CanalExampleApplication.class, args);
    }



}
