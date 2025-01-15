package com.mx.bbase.orderpayment.api.controllers;

import com.mx.bbase.orderpayment.model.request.OrderRequest;
import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import com.mx.bbase.orderpayment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService service;

    @GetMapping(path = "{orderId}")
    public ResponseEntity<OrderDTO> findOneById(@PathVariable int orderId) {
        log.debug("Request [GET]: {}", orderId);

        var orderDTO = service.findOneById(orderId);

        if (orderDTO != null)
            return ResponseEntity.ok(orderDTO);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody @Valid OrderRequest request) {
        log.debug("Request [POST] - {}", request);

        var ordenRegistrada = service.create(buildSaveDto(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenRegistrada);
    }

    @PatchMapping(path = "{orderId}")
    public ResponseEntity<OrderDTO> update(@PathVariable int orderId, @RequestParam String estado) {
        log.debug("Request [PATCH] - orden ID {}, estado {}", orderId, estado);

        var orderDTO = service.updateById(buildUpdateDto(orderId, estado));
        return ResponseEntity.ok(orderDTO);
    }

    private OrderDTO buildSaveDto(OrderRequest request) {
        return new OrderDTO(
                null,
                truncate(request.getConcepto()),
                request.getCantidadProductos(),
                truncate(request.getNombreOrdenante()),
                truncate(request.getNombreBeneficiario()),
                request.getMonto(),
                request.getEstado().toUpperCase()
        );
    }

    private OrderDTO buildUpdateDto(int orderId, String estado) {
        return new OrderDTO(orderId, estado.toUpperCase());
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        return value.length() > 40 ? value.substring(0, 40) : value;
    }

}
