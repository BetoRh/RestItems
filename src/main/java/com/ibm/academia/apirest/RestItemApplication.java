package com.ibm.academia.apirest;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.ibm.academia.apirest.commons.models.entity"})
public class RestItemApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(RestItemApplication.class, args);
	}

}
