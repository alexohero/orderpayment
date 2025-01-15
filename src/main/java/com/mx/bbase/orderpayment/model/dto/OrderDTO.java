package com.mx.bbase.orderpayment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Integer id;
    private String concepto;
    private Integer cantidadProductos;
    private String nombreOrdenante;
    private String nombreBeneficiario;
    private BigDecimal monto;
    private String estado;

    public OrderDTO(int id) {
        this.id = id;
    }

    public OrderDTO(int orderId, String estado) {
        this.id = orderId;
        this.estado = estado;
    }
}
