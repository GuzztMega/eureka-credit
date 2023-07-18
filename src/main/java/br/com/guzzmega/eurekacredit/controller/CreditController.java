package br.com.guzzmega.eurekacredit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("credits")
public class CreditController {

	@GetMapping("/health")
	public String status(){
		log.info("Checking credits microservice status...");
		return "Credits Application Status: UP!";
	}
}
