package com.mx.bbase.orderpayment.configs;

import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class KafkaTestConfig {

    @Bean
    public MockProducer<String, OrderDTO> mockProducer() {
        return new MockProducer<>(true, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public ProducerFactory<String, OrderDTO> producerFactory(MockProducer<String, OrderDTO> mockProducer) {
        return new DefaultKafkaProducerFactory<>(producerConfigs()) {
            @Override
            public org.apache.kafka.clients.producer.Producer<String, OrderDTO> createProducer() {
                return mockProducer;
            }
        };
    }

    @Bean
    public KafkaTemplate<String, OrderDTO> kafkaTemplate(ProducerFactory<String, OrderDTO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
}

