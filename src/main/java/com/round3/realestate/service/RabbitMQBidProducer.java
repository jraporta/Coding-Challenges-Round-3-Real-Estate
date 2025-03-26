package com.round3.realestate.service;

import com.round3.realestate.entity.Bid;

public interface RabbitMQBidProducer {

    void sendMessage(Bid bid);

}
