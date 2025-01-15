package com.mx.bbase.orderpayment.persistence.repositories;

import com.mx.bbase.orderpayment.model.dto.OrderDTO;

public interface OrderEventRepository {
    OrderDTO storeSaveOrder(OrderDTO dto);
    OrderDTO storeFindOrder(int orderId);
    OrderDTO storeUpdateOrder(OrderDTO dto);
}
