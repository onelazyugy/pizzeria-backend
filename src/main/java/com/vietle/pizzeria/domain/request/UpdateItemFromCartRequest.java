package com.vietle.pizzeria.domain.request;

import com.vietle.pizzeria.domain.Wing;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemFromCartRequest {
    private String enc;
    private String type;
    private int originalSelectedQty;
    private String originalSelectedFlavor;
    private int originalNumberOfOrder;
    private Wing wing;
}