package com.mx.bbase.orderpayment.service.impl;

import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import com.mx.bbase.orderpayment.persistence.repositories.OrderEventRepository;
import com.mx.bbase.orderpayment.messaging.KafkaProducerService;
import com.mx.bbase.orderpayment.service.OrderService;
import com.mx.bbase.orderpayment.validators.OrderSaveValidator;
import com.mx.bbase.orderpayment.validators.OrderUpdateValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderSaveValidator saveValidator;
    private final OrderUpdateValidator updateValidator;
    private final OrderEventRepository repository;
    private final KafkaProducerService kafkaService;

    @Override
    public OrderDTO create(OrderDTO dto) {
        log.info("Procesando orden a guardar.");

        saveValidator.validate(dto);
        return repository.storeSaveOrder(dto);
    }

    @Override
    public OrderDTO findOneById(int orderId) {
        log.info("Buscando orden por ID.");
        return repository.storeFindOrder(orderId);
    }

    @Override
    public OrderDTO updateById(OrderDTO dto) {
        log.info("Procesando petición de actualización a orden.");

        updateValidator.validate(dto);
        var orderDto = repository.storeUpdateOrder(dto);
        kafkaService.enviarMensaje(orderDto);
        return orderDto;
    }

}
