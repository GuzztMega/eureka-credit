package br.com.guzzmega.eurekacredit.service;

import br.com.guzzmega.eurekacredit.controller.exception.CustomerNotFoundException;
import br.com.guzzmega.eurekacredit.controller.exception.MicroserviceCommunicationErrorException;
import br.com.guzzmega.eurekacredit.domain.*;
import br.com.guzzmega.eurekacredit.infra.client.CardClient;
import br.com.guzzmega.eurekacredit.infra.client.CustomerClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditService {

	@Autowired
	private CustomerClient customerClient;

	@Autowired
	private CardClient cardClient;

	public CustomerStatus getCustomerStatus(String document) throws CustomerNotFoundException, MicroserviceCommunicationErrorException{
		try{
			ResponseEntity<Customer> customerResponse = customerClient.getCustomer(document);
			ResponseEntity<List<Card>> cardResponse = cardClient.getCardByClient(document);

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
}