package com.challenge.faire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FaireChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaireChallengeApplication.class, args.length > 0 ? args[0] : "--faire_token=ANY");
    }

}
