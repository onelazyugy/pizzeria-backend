package com.vietle.pizzeria.domain.request;

import com.vietle.pizzeria.domain.Wing;
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
    private Wing wing;
}
