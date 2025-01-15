package com.mx.bbase.orderpayment.validators;

import com.mx.bbase.orderpayment.exceptions.InvalidObjectException;
import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import com.mx.bbase.orderpayment.service.CommonServiceValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class OrderSaveValidator implements CommonServiceValidator {

    @Override
    public void validate(OrderDTO dto) {
        log.info("Validando datos de entrada.");

        if (StringUtils.isEmpty(dto.getNombreOrdenante()) || !validaCadena(dto.getNombreOrdenante()))
            throw new InvalidObjectException("Nombre del ordenante invalido.");
        if (StringUtils.isEmpty(dto.getNombreBeneficiario()) || !validaCadena(dto.getNombreBeneficiario()))
            throw new InvalidObjectException("Nombre del beneficiario invalido.");
        if (StringUtils.isEmpty(dto.getConcepto()) || !validaCadena(dto.getConcepto()))
            throw new InvalidObjectException("Concepto invalido.");
        if (StringUtils.isNotEmpty(dto.getEstado()) && !validaEstado(dto.getEstado()))
            throw new InvalidObjectException("Estado invalido.");
        if (dto.getMonto() == null || (dto.getMonto().compareTo(BigDecimal.ZERO) <= 0))
            throw new InvalidObjectException("Monto invalido.");
        if (dto.getCantidadProductos() <= 0)
            throw new InvalidObjectException("Cantidad de productos invalido.");

        log.info("Validación de entrada exitoso.");
    }

}
