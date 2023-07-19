package br.com.guzzmega.eurekacredit.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Card {
	private String name;
	private String cardBrand;
	private BigDecimal basicLimit;

	public Card(String name, String cardBrand, BigDecimal basicLimit){
		this.name = name;
		this.cardBrand = cardBrand;
		this.basicLimit = basicLimit;
	}
}
