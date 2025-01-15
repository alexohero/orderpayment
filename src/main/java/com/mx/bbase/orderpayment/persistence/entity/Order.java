package com.mx.bbase.orderpayment.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "bbase_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 40)
    private String concepto;
    private int cantidadProductos;

    @Column(length = 40)
    private String nombreOrdenante;

    @Column(length = 40)
    private String nombreBeneficiario;

    @Column(scale = 2)
    private BigDecimal monto;

    @Column(length = 12)
    private String estado;

}
