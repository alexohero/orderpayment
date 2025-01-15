package com.mx.bbase.orderpayment.model.request;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {

    private String concepto;
    private Integer cantidadProductos;
    private String nombreOrdenante;
    private String nombreBeneficiario;
    private BigDecimal monto;
    private String estado;

}
