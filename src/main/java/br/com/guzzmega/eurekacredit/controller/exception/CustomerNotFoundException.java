package br.com.guzzmega.eurekacredit.controller.exception;

public class CustomerNotFoundException extends Exception {

	public CustomerNotFoundException(String document){
		super(String.format("Customer data couldn't be found for the document %s", document));
	}
}
