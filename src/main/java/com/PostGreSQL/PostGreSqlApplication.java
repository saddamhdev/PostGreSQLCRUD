package com.PostGreSQL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.PostGreSQL.model")   // adjust to actual entity package
@EnableJpaRepositories("com.PostGreSQL.repository")
public class PostGreSqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostGreSqlApplication.class, args);
	}
}
