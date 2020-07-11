package com.vietle.pizzeria.domain.response;

import com.vietle.pizzeria.domain.Status;
import lombok.Builder;
import lombok.Data;

@Data
public class AddPizzaToCartResponse extends Response{
    private boolean success;
    private int totalItemInCart;

    @Builder
    public AddPizzaToCartResponse(boolean success, int totalItemInCart, Status status) {
        super(status);
        this.success = success;
        this.totalItemInCart = totalItemInCart;
    }
}
