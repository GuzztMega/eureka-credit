package br.com.guzzmega.eurekacredit.infra.client;

import br.com.guzzmega.eurekacredit.domain.Card;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "eureka-card", path = "/cards")
public interface CardClient {

	@GetMapping(params = "document")
	ResponseEntity<List<Card>> getCardByClient(@RequestParam("document") String document);

	@GetMapping(params = "income")
	ResponseEntity<List<Card>> getCardByIncome(@RequestParam("income") Long income);
}
