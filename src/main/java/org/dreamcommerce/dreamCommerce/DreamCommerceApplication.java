package org.dreamcommerce.dreamCommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DreamCommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamCommerceApplication.class, args);
	}

}
