package com.round3.realestate.service;

import com.round3.realestate.config.RabbitMQConfig;
import com.round3.realestate.entity.Bid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQBidProducerImp implements RabbitMQBidProducer {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQBidProducerImp(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(Bid bid) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, bid);
    }
}
