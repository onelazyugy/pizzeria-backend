package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.User;
import com.vietle.pizzeria.exception.PizzeriaException;

public interface UserRepository {
//    boolean registerUser(User user) throws PizzeriaException;
//    User retrieve(String email) throws PizzeriaException;
//    User save(User user) throws PizzeriaException;
//    User retrieve(User user) throws PizzeriaException;

    boolean save(User user) throws PizzeriaException;
    boolean delete(int id) throws PizzeriaException;
    boolean update(User user) throws PizzeriaException;
    User retrieve(int id) throws PizzeriaException;
    User retrieve(String email) throws PizzeriaException;
    User retrieve(User user) throws PizzeriaException;
}
