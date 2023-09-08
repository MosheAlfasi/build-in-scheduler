package com.lym.buildin.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.lym.buildin.scheduler", "com.myl.buildin.libs" })
public class BuildInSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildInSchedulerApplication.class, args);
	}

}
