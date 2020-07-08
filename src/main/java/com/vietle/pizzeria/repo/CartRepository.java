package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.Cart;
import com.vietle.pizzeria.domain.request.RemoveItemFromCartRequest;
import com.vietle.pizzeria.domain.request.UpdateItemFromCartRequest;
import com.vietle.pizzeria.exception.PizzeriaException;

public interface CartRepository {
    int save(Cart cart) throws PizzeriaException;
    Cart update(UpdateItemFromCartRequest object) throws PizzeriaException;
    Cart get(int userId) throws PizzeriaException;
    Cart remove(RemoveItemFromCartRequest object) throws PizzeriaException;
}
