package com.mx.bbase.orderpayment.validators;

import com.mx.bbase.orderpayment.exceptions.InvalidObjectException;
import com.mx.bbase.orderpayment.exceptions.ObjectNotFoundException;
import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import com.mx.bbase.orderpayment.persistence.stores.OrderStore;
import com.mx.bbase.orderpayment.service.CommonServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderUpdateValidator implements CommonServiceValidator {

    private final OrderStore store;

    @Override
    public void validate(OrderDTO dto) {
        log.info("Validando datos de actualización.");

        var id = dto.getId();
        var estado = dto.getEstado();

        store.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Producto no encontrado con id " + id));

        if (StringUtils.isNotEmpty(estado) && !validaCadena(estado))
            throw new InvalidObjectException("Estado invalido.");
        else
            validaEstado(estado);

        log.info("Validación de actualización exitosa.");
    }
}
