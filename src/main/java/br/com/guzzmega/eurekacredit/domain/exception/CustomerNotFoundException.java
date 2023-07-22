package br.com.guzzmega.eurekacredit.domain.exception;

public class CustomerNotFoundException extends Exception {

	public CustomerNotFoundException(String document){
		super(String.format("Customer data couldn't be found for the document %s", document));
	}
}
