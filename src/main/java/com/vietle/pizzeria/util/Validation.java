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
        }
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

    public static void validateAddPizzaToCart(AddPizzaToCartRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("rquest info is missing", 400);
        }
        boolean isUserIdInvalid = request.getUserId()<0?true:false;
        boolean isImgEmpty = StringUtils.isEmpty(request.getImg());
        boolean isOrderTypeEmpty = StringUtils.isEmpty(request.getOrderType());
        boolean isPizzaSizeInvalid = StringUtils.isEmpty(request.getSelectedPizzaSize());
        if(isUserIdInvalid || isImgEmpty || isOrderTypeEmpty || isPizzaSizeInvalid) {
            throw new PizzeriaException("invalid request info", 400);
        }
    }

    public static void validateRetrieveCart(RetrieveCartRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("cart request info is required", 400);
        }
        boolean isUserIdEmpty = StringUtils.isEmpty(request.getUserId())?true:false;
        if(isUserIdEmpty) {
            throw new PizzeriaException("all fields are required", 400);
        }
    }

    public static void valiateRemoveItemFromCart(RemoveItemFromCartRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("remove item from cart request is required", 400);
        }
        boolean isEncEmpty = StringUtils.isEmpty(request.getEnc());
        boolean isTypeEmpty = StringUtils.isEmpty(request.getType());
        boolean isWingNull = request.getWing() == null;
        if(isEncEmpty || isWingNull || isTypeEmpty){
            throw new PizzeriaException("invalid request info", 400);
        }
    }

    public static void validateUpdateItemFromCart(UpdateItemFromCartRequest request) throws PizzeriaException {
        if(request == null) {
            throw new PizzeriaException("update item from cart request is required", 400);
        }
        boolean isOriginalNumberOfOrderInvalid = request.getOriginalNumberOfOrder()<1?true:false;
        boolean isOriginalSelectedQtyInvalid = request.getOriginalSelectedQty()<6?true:false;
        boolean isOriginalSelctedFlavorEmpty = false;
        if(request.getOriginalSelectedFlavor() == null) {
            isOriginalNumberOfOrderInvalid = false; //this is for wing w/out flavor, we don't want to validate it
        } else {
            isOriginalNumberOfOrderInvalid = StringUtils.isEmpty(request.getOriginalSelectedFlavor());
        }
        boolean isEncEmpty = StringUtils.isEmpty(request.getEnc());
        boolean isTypeEmpty = StringUtils.isEmpty(request.getType());
        if(request.getWing() == null) {
            throw new PizzeriaException("the item to be updated is required", 400);
        }
        boolean wingIdInvalid = request.getWing().getWingId()<0?true:false;
        boolean nameEmpty = StringUtils.isEmpty(request.getWing().getName());
        boolean descEmpty = StringUtils.isEmpty(request.getWing().getDesc());
        boolean imgEmpty = StringUtils.isEmpty(request.getWing().getImg());
        boolean selectedPriceInvalid = request.getWing().getSelectedPrice()==null?true:false;
        boolean selectedQtyInvalid = request.getWing().getSelectedQty()<6?true:false;
        boolean selectedFlavorEmpty;
        if(request.getWing().getSelectedFlavor() == null) {
            selectedFlavorEmpty = false; //this is for wing w/out flavor, we don't want to validate it
        } else {
            selectedFlavorEmpty = StringUtils.isEmpty(request.getWing().getSelectedFlavor());
        }
        boolean isInvalidNumberOrOrder = false;
        if(request.getWing().getNumberOfOrder() == 0 || request.getWing().getNumberOfOrder() < 0) {
            isInvalidNumberOrOrder = true;
        }
        boolean hasFlavor = request.getWing().isHasFlavor();
        boolean isInvalidHasFlavor = true;
        if(hasFlavor == true || hasFlavor == false) {
            isInvalidHasFlavor = false;
        }
        if(isOriginalNumberOfOrderInvalid || isOriginalSelectedQtyInvalid || isOriginalSelctedFlavorEmpty || isEncEmpty || isTypeEmpty || wingIdInvalid || nameEmpty || descEmpty || imgEmpty || selectedPriceInvalid
                || selectedQtyInvalid || selectedFlavorEmpty || isInvalidNumberOrOrder || isInvalidHasFlavor) {
            throw new PizzeriaException("invalid request info", 400);
        }
    }
}
