package com.vietle.pizzeria.service;

import com.vietle.pizzeria.Constant;
import com.vietle.pizzeria.domain.Cart;
import com.vietle.pizzeria.domain.Status;
import com.vietle.pizzeria.domain.Wing;
import com.vietle.pizzeria.domain.request.AddWingToCartRequest;
import com.vietle.pizzeria.domain.request.RetrieveCartRequest;
import com.vietle.pizzeria.domain.response.AddWingToCartResponse;
import com.vietle.pizzeria.domain.response.RetrieveCartResponse;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.repo.CartRepository;
import com.vietle.pizzeria.util.PizzeriaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    private static Logger LOG = LoggerFactory.getLogger(CartService.class);
    @Autowired
    private CartRepository cartRepository;

    public AddWingToCartResponse addWingToCart(AddWingToCartRequest request) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        Wing wing = Wing.builder().desc(request.getDesc()).img(request.getImg())
                .name(request.getName()).selectedFlavor(request.getSelectedFlavor())
                .selectedPrice(request.getSelectedPrice()).selectedQty(request.getSelectedQty())
                .wingId(request.getWingId()).numberOfOrder(1).hasFlavor(request.isHasFlavor()).build();
        List<Wing> wings = new ArrayList<>(1);
        wings.add(wing);
        Cart cart = Cart.builder().userId(request.getUserId()).wings(wings).build();
        int totalCountInCart = this.cartRepository.save(cart);
        Status status = Status.builder().statusCd(200).message(Constant.SUCCESS).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
        AddWingToCartResponse response = AddWingToCartResponse.builder().status(status).success(true).totalItemInCart(totalCountInCart).build();
        return response;
    }

    public RetrieveCartResponse retrieveCart(RetrieveCartRequest request, boolean isGetCountOnly) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        int userId = request.getUserId();
        String email = request.getEmail();//TODO:
        Cart cart = this.cartRepository.get(userId);
        if(cart != null) {
            Status status = Status.builder().statusCd(200).message(Constant.SUCCESS).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
            //TODO: getPizza() size later
            RetrieveCartResponse response;
            if(!isGetCountOnly) {
                response = RetrieveCartResponse.builder().status(status).success(true).cart(cart).totalItemInCart(cart.getWings().size()).build();
            } else {
                response = RetrieveCartResponse.builder().status(status).success(true).cart(null).totalItemInCart(cart.getWings().size()).build();
            }
            return response;
        } else {
            Status status = Status.builder().statusCd(200).message("no item in cart").transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
            RetrieveCartResponse response = RetrieveCartResponse.builder().status(status).success(true).cart(null).totalItemInCart(0).build();
            return response;
        }
    }
}
