package com.vietle.pizzeria.controller;

import com.vietle.pizzeria.domain.request.AddPizzaToCartRequest;
import com.vietle.pizzeria.domain.request.AddWingToCartRequest;
import com.vietle.pizzeria.domain.request.RemoveItemFromCartRequest;
import com.vietle.pizzeria.domain.request.RetrieveCartRequest;
import com.vietle.pizzeria.domain.request.UpdateItemFromCartRequest;
import com.vietle.pizzeria.domain.response.*;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.service.CartService;
import com.vietle.pizzeria.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/cart")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add/wing")
    public ResponseEntity<AddWingToCartResponse> addWingToCart(@RequestBody AddWingToCartRequest addWingToCartRequest) throws PizzeriaException {
//        throw new PizzeriaException("unable to add item to cart", 400);
        Validation.validateAddWingOrderToCart(addWingToCartRequest);
        AddWingToCartResponse response = this.cartService.addWingToCart(addWingToCartRequest);
        ResponseEntity<AddWingToCartResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/add/pizza")
    public ResponseEntity<AddPizzaToCartResponse> addPizzaToCart(@RequestBody AddPizzaToCartRequest addPizzaToCartRequest) throws PizzeriaException {
//                throw new PizzeriaException("unable to retrieve item from cart at this time [500]", 500);
//                throw new PizzeriaException("unable to retrieve item at this time [400]", 400);
//        throw new PizzeriaException("unable to retrieve cart at this time [403]", 403);

        Validation.validateAddPizzaToCart(addPizzaToCartRequest);
        AddPizzaToCartResponse response = this.cartService.addPizzaToCart(addPizzaToCartRequest);
        ResponseEntity<AddPizzaToCartResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/retrieve")
    public ResponseEntity<RetrieveCartResponse> retrieveCart(@RequestBody RetrieveCartRequest retrieveCartRequest, @RequestParam(required = false) boolean isGetCountOnly) throws PizzeriaException {
//                throw new PizzeriaException("unable to retrieve item from cart at this time [500]", 500);
//                throw new PizzeriaException("unable to retrieve item at this time [400]", 400);
//        throw new PizzeriaException("unable to retrieve cart at this time [403]", 403);

        Validation.validateRetrieveCart(retrieveCartRequest);
        RetrieveCartResponse response = this.cartService.retrieveCart(retrieveCartRequest, isGetCountOnly);
        ResponseEntity<RetrieveCartResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/remove")
    public ResponseEntity<RemoveItemFromCartResponse> removeItemFromCart(@RequestBody RemoveItemFromCartRequest removeItemFromCartRequest) throws PizzeriaException {
//        throw new PizzeriaException("unable to remove item from cart at this time [500]", 500);
//        throw new PizzeriaException("unable to remove item at this time [400]", 400);
//        throw new PizzeriaException("unable to remove item from cart at this time [403]", 403);
        Validation.valiateRemoveItemFromCart(removeItemFromCartRequest);
        RemoveItemFromCartResponse response = this.cartService.removeItemFromCart(removeItemFromCartRequest);
        ResponseEntity<RemoveItemFromCartResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateItemFromCartResponse> updateItemInCart(@RequestBody UpdateItemFromCartRequest updateItemFromCartRequest) throws PizzeriaException {
        log.info("/update api");
//        throw new PizzeriaException("unable to remove item from cart at this time [500]", 500);
//        throw new PizzeriaException("unable to remove item at this time [400]", 400);
//        throw new PizzeriaException("unable to remove item from cart at this time [403]", 403);
        Validation.validateUpdateItemFromCart(updateItemFromCartRequest);
        UpdateItemFromCartResponse resposne = this.cartService.updateItemFromCart(updateItemFromCartRequest);
        ResponseEntity<UpdateItemFromCartResponse> responseEntity = new ResponseEntity<>(resposne, HttpStatus.OK);
        return responseEntity;
    }
}
