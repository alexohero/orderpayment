package com.mx.bbase.orderpayment.service;

import com.mx.bbase.orderpayment.model.dto.OrderDTO;

public interface OrderService {
    OrderDTO create(OrderDTO dto);
    OrderDTO findOneById(int orderId);
    OrderDTO updateById(OrderDTO dto);
}
