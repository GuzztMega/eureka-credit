package br.com.guzzmega.eurekacredit.domain.exception;

import lombok.Getter;

public class MicroserviceCommunicationErrorException extends Exception {

	@Getter
	private Integer status;

	public MicroserviceCommunicationErrorException(String message, Integer status){
		super(message);
		this.status = status;
	}
}
