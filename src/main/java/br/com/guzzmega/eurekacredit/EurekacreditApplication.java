package br.com.guzzmega.eurekacredit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekacreditApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekacreditApplication.class, args);
	}

}
