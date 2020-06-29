package com.vietle.pizzeria.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Wing {
    private int wingId;
    private String name;
    private String desc;
    private String img;
    private BigDecimal selectedPrice;
    private int selectedQty;
    private String selectedFlavor;
    private int numberOfOrder;
    private boolean hasFlavor;
}
