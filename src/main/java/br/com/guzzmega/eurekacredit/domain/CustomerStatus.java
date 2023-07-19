package br.com.guzzmega.eurekacredit.domain;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CustomerStatus {
	private Customer customer;
	private List<Card> cardList;
}