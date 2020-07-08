package com.vietle.pizzeria.domain;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
