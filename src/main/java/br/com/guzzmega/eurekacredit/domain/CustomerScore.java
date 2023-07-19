package br.com.guzzmega.eurekacredit.domain;

import lombok.Data;

import java.util.List;

@Data
public class CustomerScore {
	private List<Card> approvedCardList;

	public CustomerScore(List<Card> approvedCardList){
		this.approvedCardList = approvedCardList;
	}
}