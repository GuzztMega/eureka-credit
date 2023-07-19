package br.com.guzzmega.eurekacredit.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {
	private UUID id;
	private String name;
	private Integer age;
}
