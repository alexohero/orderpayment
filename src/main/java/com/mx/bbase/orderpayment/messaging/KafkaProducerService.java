package com.mx.bbase.orderpayment.messaging;

import com.mx.bbase.orderpayment.model.dto.OrderDTO;

public interface KafkaProducerService {

    void enviarMensaje(OrderDTO dto);

}
