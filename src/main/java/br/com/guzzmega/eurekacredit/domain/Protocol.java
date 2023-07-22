package br.com.guzzmega.eurekacredit.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Protocol {

	private UUID id;
	private LocalDateTime creationDatetime;

	public Protocol(UUID id, LocalDateTime creationDatetime){
		this.id = id;
		this.creationDatetime = creationDatetime;
	}
}

