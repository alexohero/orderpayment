package com.mx.bbase.orderpayment.messaging;

import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @Override
    public void enviarMensaje(OrderDTO dto) {
        log.info("Notificando actualización de estado de orden.");
        CompletableFuture<SendResult<String, OrderDTO>> future = kafkaTemplate.send(topic, dto);
        future.whenCompleteAsync((sendResult,throwable) -> {
            if (throwable != null) {
                throw new RuntimeException(throwable);
            }
            log.info("Se ha notificado el cambio de estado a {} de la orden con ID {} en el topico {}",
                    sendResult.getProducerRecord().value().getEstado(),
                    sendResult.getProducerRecord().value().getId(),
                    sendResult.getRecordMetadata().topic());
        });
    }
}

