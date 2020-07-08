package com.vietle.pizzeria.domain.response;

import com.vietle.pizzeria.domain.Cart;
import com.vietle.pizzeria.domain.CartSummary;
import com.vietle.pizzeria.domain.Status;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateItemFromCartResponse extends Response {
    private boolean success;
    private Cart cart;
    private int totalItemInCart;
    private CartSummary cartSummary;

    @Builder
    public UpdateItemFromCartResponse(boolean success, Cart cart, int totalItemInCart, CartSummary cartSummary, Status status) {
        super(status);
        this.success = success;
        this.cart = cart;
        this.totalItemInCart = totalItemInCart;
        this.cartSummary = cartSummary;
    }
}
