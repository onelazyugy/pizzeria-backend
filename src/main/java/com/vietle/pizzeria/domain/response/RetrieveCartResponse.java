package com.vietle.pizzeria.domain.response;

import com.vietle.pizzeria.domain.Cart;
import com.vietle.pizzeria.domain.Status;
import lombok.Builder;
import lombok.Data;

@Data
public class RetrieveCartResponse extends Response{
    private boolean success;
    private Cart cart;
    private int totalItemInCart;
    @Builder
    public RetrieveCartResponse(boolean success, Status status, Cart cart, int totalItemInCart) {
        super(status);
        this.success = success;
        this.cart = cart;
        this.totalItemInCart = totalItemInCart;
    }
}
