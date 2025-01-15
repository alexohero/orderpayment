package com.mx.bbase.orderpayment.service;

import com.mx.bbase.orderpayment.exceptions.ObjectNotFoundException;
import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import com.mx.bbase.orderpayment.persistence.enums.OrderStatus;
import org.apache.commons.lang3.StringUtils;

public interface CommonServiceValidator {

    void validate(OrderDTO dto);

    default boolean validaEstado(String estado) {
        try {
            if (StringUtils.isEmpty(estado))
                return true;
            OrderStatus.valueOf(estado);
            return true;
        } catch (IllegalArgumentException e) {
            throw new ObjectNotFoundException("Estado no reconocido: " + estado);
        }
    }

    default boolean validaCadena(String input) {
        String regex = "^[a-zA-Z0-9\\sáéíóúÁÉÍÓÚ]+$";
        return input.matches(regex);
    }

}
