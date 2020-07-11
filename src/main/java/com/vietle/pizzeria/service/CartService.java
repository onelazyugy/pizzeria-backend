package com.vietle.pizzeria.service;

import com.vietle.pizzeria.constant.Constant;
import com.vietle.pizzeria.domain.*;
import com.vietle.pizzeria.domain.request.*;
import com.vietle.pizzeria.domain.response.*;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.price.PriceService;
import com.vietle.pizzeria.repo.CartRepository;
import com.vietle.pizzeria.util.PizzeriaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class CartService {
    private static BigDecimal TAX_PERCENTAGE = new BigDecimal(0.07);
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PriceService priceService;

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

    public AddPizzaToCartResponse addPizzaToCart(AddPizzaToCartRequest addPizzaToCartRequest) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        BigDecimal pizzaPriceBasedOnSize = this.priceService.getPizzaPriceBasedOnSize(addPizzaToCartRequest.getSelectedPizzaSize());
        Pizza pizza = Pizza.builder().selectedPizzaPrice(pizzaPriceBasedOnSize.setScale(2, RoundingMode.HALF_UP))
                                    .selectedCheese(addPizzaToCartRequest.getSelectedCheese())
                                    .selectedMeat(addPizzaToCartRequest.getSelectedMeat())
                                    .selectedVeggie(addPizzaToCartRequest.getSelectedVeggie())
                                    .selectedPizzaSize(addPizzaToCartRequest.getSelectedPizzaSize())
                                    .img(addPizzaToCartRequest.getImg()).numberOfOrder(1)
                                    .orderType(addPizzaToCartRequest.getOrderType()).build();
        List<Pizza> pizzas = new ArrayList<>(1);
        pizzas.add(pizza);
        Cart cart = Cart.builder().userId(addPizzaToCartRequest.getUserId()).pizzas(pizzas).build();
        int totalCountInCart = this.cartRepository.save(cart);
        Status status = Status.builder().statusCd(200).message(Constant.SUCCESS).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
        AddPizzaToCartResponse response = AddPizzaToCartResponse.builder().status(status).success(true).totalItemInCart(totalCountInCart).build();
        return response;
    }

    public RetrieveCartResponse retrieveCart(RetrieveCartRequest request, boolean isGetCountOnly) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        int userId = request.getUserId();
        String email = request.getEmail();//TODO:
        Cart cart = this.cartRepository.get(userId);
        if(cart != null) {
            String message = Constant.SUCCESS;
            boolean isWingsEmpty = true;
            boolean isPizzasEmpty = true;
            if(cart.getWings() != null) {
                isWingsEmpty = cart.getWings().isEmpty()?true:false;
            }
            if(cart.getPizzas() != null) {
                isPizzasEmpty = cart.getPizzas().isEmpty()?true:false;
            }
            if(isWingsEmpty && isPizzasEmpty) {
                message = Constant.CART_IS_EMPTY;
            }
            if(!isPizzasEmpty) {
                cart.getPizzas().forEach(p->{
                    String randomPizzaImage = this.randomPizzaImage();
                    p.setImg(randomPizzaImage);
                });
            }
            CartSummary cartSummary = this.priceService.calculateSummary(cart);
            Status status = Status.builder().statusCd(200).message(message).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
            RetrieveCartResponse response;
            int totalItemInCart = 0;
            if(!isWingsEmpty) {
                totalItemInCart = totalItemInCart + cart.getWings().size();
            }
            if(!isPizzasEmpty) {
                totalItemInCart = totalItemInCart + cart.getPizzas().size();
            }
            if(!isGetCountOnly) {
                response = RetrieveCartResponse.builder().cartSummary(cartSummary).status(status).success(true).cart(cart).totalItemInCart(totalItemInCart).build();
            } else {
                response = RetrieveCartResponse.builder().cartSummary(null).status(status).success(true).cart(null).totalItemInCart(totalItemInCart).build();
            }
            return response;
        } else {
            Status status = Status.builder().statusCd(200).message(Constant.CART_IS_EMPTY).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
            RetrieveCartResponse response = RetrieveCartResponse.builder().status(status).success(true).cart(null).totalItemInCart(0).build();
            return response;
        }
    }

    public RemoveItemFromCartResponse removeItemFromCart(RemoveItemFromCartRequest request) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        Cart cart = this.cartRepository.remove(request);
        CartSummary cartSummary = this.priceService.calculateSummary(cart);
        //TODO: need to handle pizza as well
        String message = Constant.SUCCESS;
        int pizzaCount = 0;
        int wingCount = 0;
        if(cart.getPizzas() != null) {
            if(!cart.getPizzas().isEmpty()){
                pizzaCount = cart.getPizzas().size();
            }
        }
        if(cart.getWings() != null) {
            if(!cart.getWings().isEmpty()) {
                wingCount = cart.getWings().size();
            }
        }
        int totalItemInCart = pizzaCount + wingCount;
        Status status = Status.builder().statusCd(200).message(message).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
        RemoveItemFromCartResponse response = RemoveItemFromCartResponse.builder().cart(cart).success(true).status(status).totalItemInCart(totalItemInCart).cartSummary(cartSummary).build();
        return response;
    }

    public UpdateItemFromCartResponse updateItemFromCart(UpdateItemFromCartRequest request) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        Cart cart = this.cartRepository.update(request);
        CartSummary cartSummary = this.priceService.calculateSummary(cart);
        String message = Constant.SUCCESS;
        int pizzaCount = 0;
        int wingCount = 0;
        if(cart.getPizzas() != null) {
            if(!cart.getPizzas().isEmpty()){
                pizzaCount = cart.getPizzas().size();
            }
        }
        if(cart.getWings() != null) {
            if(!cart.getWings().isEmpty()) {
                wingCount = cart.getWings().size();
            }
        }
        int totalItemInCart = pizzaCount + wingCount;
        Status status = Status.builder().statusCd(200).message(message).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
        UpdateItemFromCartResponse response = UpdateItemFromCartResponse.builder().cart(cart).success(true).status(status).totalItemInCart(totalItemInCart).cartSummary(cartSummary).build();
        return response;
    }

    public String randomPizzaImage() {
        String[] pizzaImages = {"assets/pizza/1p.jpeg", "assets/pizza/2p.png", "assets/pizza/3p.jpg", "assets/pizza/4p.png", "assets/pizza/5p.png"};
        Random r = new Random();
        int randomNumber=r.nextInt(pizzaImages.length);
        return pizzaImages[randomNumber];
    }
}
