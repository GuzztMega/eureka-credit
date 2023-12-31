package br.com.guzzmega.eurekacredit.controller;

import br.com.guzzmega.eurekacards.domain.CardEmission;
import br.com.guzzmega.eurekacredit.domain.*;
import br.com.guzzmega.eurekacredit.domain.exception.CardEmissionErrorException;
import br.com.guzzmega.eurekacredit.domain.exception.CustomerNotFoundException;
import br.com.guzzmega.eurekacredit.domain.exception.MicroserviceCommunicationErrorException;
import br.com.guzzmega.eurekacredit.service.CreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("credits")
public class CreditController {

	@Autowired
	private CreditService service;

	@GetMapping("/health")
	public String status(){
		log.info("Checking credits microservice status...");
		return "Credits Application Status: UP!";
	}

	@GetMapping("/{document}")
	public ResponseEntity getCustomerStatus(@PathVariable(value="document") String document){
		try{
			CustomerStatus customerStatus = service.getCustomerStatus(document);
			return ResponseEntity.status(HttpStatus.OK).body(customerStatus);

		} catch(CustomerNotFoundException ex) {
			log.error("Customer data couldn't be found: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch(MicroserviceCommunicationErrorException ex) {
			log.error("Failed to communicate with client: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.resolve(ex.getStatus())).body(ex.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity postScore(@RequestBody Score score){
		try{
			CustomerScore customerScore = service.postScore(score.getDocument(), score.getIncome());
			return ResponseEntity.ok(customerScore);

		} catch(CustomerNotFoundException ex) {
			log.error("Customer data couldn't be found: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		} catch(MicroserviceCommunicationErrorException ex) {
			log.error("Failed to communicate with client: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.resolve(ex.getStatus())).body(ex.getMessage());
		}
	}

	@PostMapping("/request-card")
	public ResponseEntity requestCard(@RequestBody CardEmission cardEmission){
		try{
			Protocol protocol = service.requestCard(cardEmission);
			return ResponseEntity.ok(protocol);
		}catch(CardEmissionErrorException ex){
			log.error("Failed to request credit card: {}", ex.getMessage());
			return ResponseEntity.internalServerError().body(ex.getMessage());
		}
	}
}