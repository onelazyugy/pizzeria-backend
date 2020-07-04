package com.vietle.pizzeria.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveItemFromCartRequest {
    private String enc;
    private String type;
    private int itemId;
    private int numberOfOrder;
}
