package br.com.guzzmega.eurekacredit.domain.exception;

public class CardEmissionErrorException extends RuntimeException {
	public CardEmissionErrorException(String message){
		super(message);
	}
}