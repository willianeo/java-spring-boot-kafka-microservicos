package com.portal.api.message;

import com.portal.api.dto.CarPostDTO;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.springframework.kafka.support.serializer.JsonSerializer.ADD_TYPE_INFO_HEADERS;

@Component
@Configuration
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public ProducerFactory<String, CarPostDTO> userProducerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        // 15REGALOMLHV
        configProps.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ADD_TYPE_INFO_HEADERS, false);
        configProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, CarPostDTO> userKafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }
}
