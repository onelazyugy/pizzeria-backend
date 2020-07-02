package com.vietle.pizzeria.controller;

import com.vietle.pizzeria.domain.request.AddWingToCartRequest;
import com.vietle.pizzeria.domain.request.RemoveItemFromCartRequest;
import com.vietle.pizzeria.domain.request.RetrieveCartRequest;
import com.vietle.pizzeria.domain.response.AddWingToCartResponse;
import com.vietle.pizzeria.domain.response.RemoveItemFromCartResponse;
import com.vietle.pizzeria.domain.response.RetrieveCartResponse;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.service.CartService;
import com.vietle.pizzeria.util.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private static Logger LOG = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @RequestMapping("/add/wing")
    public ResponseEntity<AddWingToCartResponse> addWingToCart(@RequestBody AddWingToCartRequest addWingToCartRequest) throws PizzeriaException {
//        throw new PizzeriaException("unable to add item to cart", 400);
        Validation.validateAddWingOrderToCart(addWingToCartRequest);
        AddWingToCartResponse response = this.cartService.addWingToCart(addWingToCartRequest);
        ResponseEntity<AddWingToCartResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping("/retrieve")
    public ResponseEntity<RetrieveCartResponse> retrieveCart(@RequestBody RetrieveCartRequest retrieveCartRequest, @RequestParam(required = false) boolean isGetCountOnly) throws PizzeriaException {
//        throw new PizzeriaException("unable to retrieve cart at this time", 500);
        Validation.validateRetrieveCart(retrieveCartRequest);
        RetrieveCartResponse response = this.cartService.retrieveCart(retrieveCartRequest, isGetCountOnly);
        ResponseEntity<RetrieveCartResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping("/remove")
    public ResponseEntity<RemoveItemFromCartResponse> removeItemFromCart(@RequestBody RemoveItemFromCartRequest removeItemFromCartRequest) throws PizzeriaException {
//                throw new PizzeriaException("unable to retrieve cart at this time", 500);
//                throw new PizzeriaException("unable to remove item at this time", 400);
//        throw new PizzeriaException("unable to retrieve cart at this time", 403);
        RemoveItemFromCartResponse response = this.cartService.removeItemFromCart(removeItemFromCartRequest);
        ResponseEntity<RemoveItemFromCartResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }
}
