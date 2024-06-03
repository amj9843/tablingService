package com.zerobase.tabling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TablingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TablingApplication.class, args);
	}

}
