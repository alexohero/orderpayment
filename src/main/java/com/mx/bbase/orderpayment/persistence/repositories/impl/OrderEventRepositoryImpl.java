package com.mx.bbase.orderpayment.persistence.repositories.impl;

import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import com.mx.bbase.orderpayment.persistence.entity.Order;
import com.mx.bbase.orderpayment.persistence.enums.OrderStatus;
import com.mx.bbase.orderpayment.persistence.repositories.OrderEventRepository;
import com.mx.bbase.orderpayment.persistence.stores.OrderStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderEventRepositoryImpl implements OrderEventRepository {

    private final OrderStore store;

    @Override
    public OrderDTO storeSaveOrder(OrderDTO dto) {
        var order = new Order();

        order.setConcepto(dto.getConcepto());
        order.setEstado(StringUtils.isEmpty(dto.getEstado()) ? String.valueOf(OrderStatus.CAPTURADO) : dto.getEstado());
        order.setMonto(dto.getMonto());
        order.setNombreOrdenante(dto.getNombreOrdenante());
        order.setNombreBeneficiario(dto.getNombreBeneficiario());
        order.setCantidadProductos(dto.getCantidadProductos());

        log.info("Guardando Orden.");
        store.save(order);

        return mapOrderEntityToDTO(order);
    }

    @Override
    public OrderDTO storeFindOrder(int orderId) {
        log.info("Consultando orden por ID.");
        var entity = store.findById(orderId);
        return entity.map(this::mapOrderEntityToDTO).orElse(null);
    }

    @Override
    public OrderDTO storeUpdateOrder(OrderDTO dto) {
        var order = store.findById(dto.getId()).get();
        order.setEstado(dto.getEstado());

        log.info("Actualizando orden.");
        store.save(order);

        return mapOrderEntityToDTO(order);
    }

    private OrderDTO mapOrderEntityToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setConcepto(order.getConcepto());
        orderDTO.setEstado(order.getEstado());
        orderDTO.setMonto(order.getMonto());
        orderDTO.setNombreOrdenante(order.getNombreOrdenante());
        orderDTO.setNombreBeneficiario(order.getNombreBeneficiario());
        orderDTO.setCantidadProductos(order.getCantidadProductos());

        log.debug("OrdenDTO construido. {}", orderDTO);
        return orderDTO;
    }
}
