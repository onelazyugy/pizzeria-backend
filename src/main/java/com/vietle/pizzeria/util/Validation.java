package com.vietle.pizzeria.util;

import com.vietle.pizzeria.domain.request.*;
import com.vietle.pizzeria.exception.PizzeriaException;
import org.springframework.util.StringUtils;

public class Validation {
    private Validation(){}

    public static void validateUserLoginInfo(LoginUserRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("login information is missing", 400);
        } else if(StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getPassword())) {
            throw new PizzeriaException("login information is missing", 400);
        }
    }

    public static void validateUserRegistrationInfo(RegisterUserRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("registration information is missing", 400);
        } else if(StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getPassword()) ||
                StringUtils.isEmpty(request.getConfirmPassword()) || StringUtils.isEmpty(request.getNickName())) {
            throw new PizzeriaException("all fields are required", 400);
        } else if(!request.getPassword().trim().equals(request.getConfirmPassword().trim())) {
            throw new PizzeriaException("password does not match", 400);
        }
    }

    public static void validateAddWingOrderToCart(AddWingToCartRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("cart info is missing", 400);
        } else {
            boolean descEmpty = StringUtils.isEmpty(request.getDesc());
            boolean imgEmpty = StringUtils.isEmpty(request.getImg());
            boolean nameEmpty = StringUtils.isEmpty(request.getName());
            boolean selectedFlavorEmpty = request.isHasFlavor()?StringUtils.isEmpty(request.getSelectedFlavor()):false;
            boolean selectedPriceInvalid = request.getSelectedPrice()==null?true:false;
            boolean userIdInvalid = request.getUserId()<0?true:false;
            boolean wingIdInvalaid = request.getWingId()<0?true:false;
            boolean selectedQtyInvalid = request.getSelectedQty()<6?true:false;
            if(descEmpty || imgEmpty || nameEmpty || selectedFlavorEmpty || selectedPriceInvalid || userIdInvalid || wingIdInvalaid || selectedQtyInvalid) {
                throw new PizzeriaException("all fields are required", 400);
            }
        }
    }

    public static void validateRetrieveCart(RetrieveCartRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("cart request info is required", 400);
        } else {
            boolean isUserIdEmpty = StringUtils.isEmpty(request.getUserId())?true:false;
            if(isUserIdEmpty) {
                throw new PizzeriaException("all fields are required", 400);
            }
        }
    }

    public static void valiateRemoveItemFromCart(RemoveItemFromCartRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("remove item from cart request is required", 400);
        } else {
            boolean isEncEmpty = StringUtils.isEmpty(request.getEnc());
            boolean isInvalidItemId = request.getItemId()<0?true:false;
            boolean isTypeEmpty = StringUtils.isEmpty(request.getType());
            boolean isInvalidNumberOrOrder = false;
            if(request.getNumberOfOrder() == 0 || request.getNumberOfOrder() < 0) {
                isInvalidNumberOrOrder = true;
            }
            if(isEncEmpty || isInvalidItemId || isTypeEmpty || isInvalidNumberOrOrder){
                throw new PizzeriaException("invalid request info", 400);
            }
        }
    }
}
