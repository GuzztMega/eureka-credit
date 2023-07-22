package br.com.guzzmega.eurekacredit.service;

import br.com.guzzmega.eurekacredit.domain.exception.CardEmissionErrorException;
import br.com.guzzmega.eurekacredit.domain.exception.CustomerNotFoundException;
import br.com.guzzmega.eurekacredit.domain.exception.MicroserviceCommunicationErrorException;
import br.com.guzzmega.eurekacredit.domain.*;
import br.com.guzzmega.eurekacredit.infra.client.CardClient;
import br.com.guzzmega.eurekacredit.infra.client.CustomerClient;
import br.com.guzzmega.eurekacredit.infra.mqueue.CardEmissionPublisher;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CreditService {

	@Autowired
	private CardClient cardClient;

	@Autowired
	private CustomerClient customerClient;

	@Autowired
	private CardEmissionPublisher publisher;

	public CustomerStatus getCustomerStatus(String document) throws CustomerNotFoundException, MicroserviceCommunicationErrorException{
		try{
			ResponseEntity<Customer> customerResponse = customerClient.getCustomer(document);
			ResponseEntity<List<Card>> cardResponse = cardClient.getCardByCustomer(document);

			return CustomerStatus.builder().customer(customerResponse.getBody()).cardList(cardResponse.getBody()).build();

		}catch(FeignException.FeignClientException ex){
			if(HttpStatus.NOT_FOUND.value() == ex.status()){
				throw new CustomerNotFoundException(document);
			}

			throw new MicroserviceCommunicationErrorException(ex.getMessage(), ex.status());
		}
	}

	public CustomerScore postScore(String document, Long income) throws CustomerNotFoundException, MicroserviceCommunicationErrorException{
		try{
			Customer customer = customerClient.getCustomer(document).getBody();
			List<Card> cardList = cardClient.getCardByIncome(income).getBody();

			var approvedCardList = cardList.stream().map(card -> {
				BigDecimal incomeLimitBD = card.getBasicLimit();
				BigDecimal ageBD = BigDecimal.valueOf(customer.getAge());
				BigDecimal approvedLimit = ageBD.divide(BigDecimal.valueOf(10)).multiply(incomeLimitBD);

				return new Card(card.getName(), card.getCardBrand(), approvedLimit);
			}).collect(Collectors.toList());

			return new CustomerScore(approvedCardList);

		}catch(FeignException.FeignClientException ex){
			if(HttpStatus.NOT_FOUND.value() == ex.status()){
				throw new CustomerNotFoundException(document);
			}

			throw new MicroserviceCommunicationErrorException(ex.getMessage(), ex.status());
		}
	}

	public Protocol requestCard(CardEmission cardEmission){
		try {
			publisher.requestCard(cardEmission);
			return new Protocol(UUID.randomUUID(), LocalDateTime.now());

		} catch(Exception ex) {
			throw new CardEmissionErrorException(ex.getMessage());
		}
	}
}