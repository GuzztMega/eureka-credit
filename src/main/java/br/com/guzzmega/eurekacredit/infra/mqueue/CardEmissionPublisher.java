package br.com.guzzmega.eurekacredit.infra.mqueue;

import br.com.guzzmega.eurekacredit.domain.CardEmission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardEmissionPublisher {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private Queue cardEmissionQueue;

	public void requestCard(CardEmission cardEmission) throws JsonProcessingException{
		var json = new ObjectMapper().writeValueAsString(cardEmission);
		rabbitTemplate.convertAndSend(cardEmissionQueue.getName(), json);
	}
}
