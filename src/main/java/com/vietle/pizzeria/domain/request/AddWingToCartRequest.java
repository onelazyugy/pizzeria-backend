package com.vietle.pizzeria.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddWingToCartRequest {
    private int wingId;
    private String name;
    private String desc;
    private String img;
    private BigDecimal selectedPrice;
    private int selectedQty;
    private String selectedFlavor;
    private int userId;
    private boolean hasFlavor;
}
