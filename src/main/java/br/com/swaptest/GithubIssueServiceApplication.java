package br.com.swaptest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GithubIssueServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GithubIssueServiceApplication.class, args);
    }
}
