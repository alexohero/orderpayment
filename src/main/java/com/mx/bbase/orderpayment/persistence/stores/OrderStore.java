package com.mx.bbase.orderpayment.persistence.stores;

import com.mx.bbase.orderpayment.persistence.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStore extends JpaRepository<Order, Integer> {
}
