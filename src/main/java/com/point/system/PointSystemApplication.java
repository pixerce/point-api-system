package com.point.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@EnableAsync
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.point.system.data"})
@EntityScan(basePackages = {"com.point.system.data"})
@SpringBootApplication
public class PointSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PointSystemApplication.class, args);
	}

}
