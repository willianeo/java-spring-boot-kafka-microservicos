package com.portal.api.message;

import com.portal.api.dto.CarPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerMessage {

    private final Logger LOG = LoggerFactory.getLogger(KafkaProducerMessage.class);

    @Autowired
    private KafkaTemplate<String, CarPostDTO> kafkaTemplate;

    private final String KAFKA_TOPIC = "car-post-topic";

    public void sendMessage(CarPostDTO carPostDTO){
        LOG.info("PROCUDER MESSAGE - Received Car Post information: {}", carPostDTO);
        kafkaTemplate.send(KAFKA_TOPIC, carPostDTO);
    }
}
