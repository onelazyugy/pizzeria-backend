package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.Cart;
import com.vietle.pizzeria.exception.PizzeriaException;

public interface CartRepository {
    int save(Cart cart) throws PizzeriaException;
    boolean delete(Object object) throws PizzeriaException;
    boolean update(Object object) throws PizzeriaException;
    Cart get(int userId) throws PizzeriaException;
}
