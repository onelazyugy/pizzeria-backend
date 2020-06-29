package com.vietle.pizzeria.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartSummary {
    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal total;
}
