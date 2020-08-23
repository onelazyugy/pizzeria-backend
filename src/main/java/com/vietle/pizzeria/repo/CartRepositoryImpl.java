package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.constant.Constant;
import com.vietle.pizzeria.domain.Cart;
import com.vietle.pizzeria.domain.Pizza;
import com.vietle.pizzeria.domain.Wing;
import com.vietle.pizzeria.domain.request.RemoveItemFromCartRequest;
import com.vietle.pizzeria.domain.request.UpdateItemFromCartRequest;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.service.MongCartSequenceService;
import com.vietle.pizzeria.util.HelperBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class CartRepositoryImpl implements CartRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongCartSequenceService mongCartSequenceService;
    @Autowired
    private HelperBean helperBean;

    @Override
    public int save(Cart cart) throws PizzeriaException {
//        int userId = Integer.parseInt(this.helperBean.decrypt(cart.get)); //TODO:
        int nextSequence = mongCartSequenceService.getNextSequence("sequence");
        cart.setId(nextSequence);
        if(cart.getWings() != null && cart.getWings().size() > 0) {
            return saveWingOrderToCart(cart);
        } else if(cart.getPizzas() != null && cart.getPizzas().size() > 0) {
            //save pizza order to cart
            return savePizzaOrderTocart(cart);
        } else {
            throw new PizzeriaException("unknown cart, unable to save item to cart", 400);
        }
    }

    private int savePizzaOrderTocart(Cart cart) throws PizzeriaException{
        int totalWingOrderInCart = 0;
        int sumOfWingAndPizzaInCart;
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(cart.getUserId()));
        List<Cart> queriedCartList = this.mongoTemplate.find(query, Cart.class);
        if(queriedCartList != null && queriedCartList.size()>0) {
            if(queriedCartList.get(0).getWings() != null && queriedCartList.get(0).getWings().size()>0) {
                int currentWingOderInCart = queriedCartList.get(0).getWings().size();
                totalWingOrderInCart = totalWingOrderInCart + currentWingOderInCart;
            }
            //a pizza list already exist in this user's cart
            if(queriedCartList.get(0).getPizzas() != null && queriedCartList.get(0).getPizzas().size()>0) {
                //add the new pizza to the current pizza list
                List<Pizza> currentListOfPizzas = queriedCartList.get(0).getPizzas();

                //if an existing pizza equal to the new one, than increment its numberOfOrder to 1
                boolean foundAMatchPizza = false;
                for(Pizza pizza: currentListOfPizzas) {
                    if(pizza.equals(cart.getPizzas().get(0))) {
                        pizza.setNumberOfOrder(pizza.getNumberOfOrder() + 1);
                        mongoTemplate.save(queriedCartList.get(0));//always be 1 per user
                        foundAMatchPizza = true;
                    }
                }
                if(!foundAMatchPizza) {
                    queriedCartList.get(0).getPizzas().add(cart.getPizzas().get(0));
                    mongoTemplate.save(queriedCartList.get(0));
                }
                int currentPizzaOrderInCart = currentListOfPizzas.size();
                sumOfWingAndPizzaInCart = totalWingOrderInCart + currentPizzaOrderInCart;
            }
            // this user's cart does not have pizza list yet
            else {
                //take existing cart and add pizza if existing cart exist
                Cart cartToSave = queriedCartList.get(0);
                cartToSave.setPizzas(cart.getPizzas());
                mongoTemplate.save(cartToSave);
                sumOfWingAndPizzaInCart = 1 + totalWingOrderInCart;//only 1 pizza to save
            }
        } else {
            mongoTemplate.save(cart);
            sumOfWingAndPizzaInCart = 1;
        }
        return sumOfWingAndPizzaInCart;
    }

    private int saveWingOrderToCart(Cart cart) {
        int totalPizzaOrderInCart = 0;
        int sumOfWingAndPizzaInCart;
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(cart.getUserId()));
        List<Cart> queriedCartList = this.mongoTemplate.find(query, Cart.class);
        if(queriedCartList != null && queriedCartList.size() > 0) {
            if(queriedCartList.get(0).getPizzas() != null && queriedCartList.get(0).getPizzas().size()>0) {
                int currentPizzaOderInCart = queriedCartList.get(0).getPizzas().size();
                totalPizzaOrderInCart = totalPizzaOrderInCart + currentPizzaOderInCart;
            }

            Wing wingToBeSavedToCart = cart.getWings().get(0);
            //list of wing exist, add new wing to existing wing list
            if(queriedCartList.get(0) != null && queriedCartList.get(0).getWings() != null && !queriedCartList.get(0).getWings().isEmpty()) {
                List<Wing> currentListOfWingsInCart = queriedCartList.get(0).getWings();
                Collections.sort(currentListOfWingsInCart, Comparator.comparing(Wing::getWingId));
                boolean matched = false;
                for(Wing wing: currentListOfWingsInCart) {
                    // wing with flavor option
                    if(wing.getWingId() == wingToBeSavedToCart.getWingId() &&
                            wing.isHasFlavor() && wingToBeSavedToCart.isHasFlavor() &&
                            wing.getSelectedQty() == wingToBeSavedToCart.getSelectedQty() &&
                            wing.getSelectedFlavor().equals(wingToBeSavedToCart.getSelectedFlavor()) &&
                            wing.getSelectedPrice().compareTo(wingToBeSavedToCart.getSelectedPrice()) == 0) {
                        int currentOrderCount = wing.getNumberOfOrder();
                        wing.setNumberOfOrder(currentOrderCount+1);
                        matched = true;
                    }
                    // wing with no flavor option
                    else if(!wingToBeSavedToCart.isHasFlavor() && !wing.isHasFlavor() && wing.getWingId() == wingToBeSavedToCart.getWingId() &&
                            wing.getSelectedPrice().compareTo(wingToBeSavedToCart.getSelectedPrice()) == 0 && wing.getSelectedQty() == wingToBeSavedToCart.getSelectedQty()) {
                        int currentOrderCount = wing.getNumberOfOrder();
                        wing.setNumberOfOrder(currentOrderCount+1);
                        matched = true;
                    }
                }
                if(!matched){
                    currentListOfWingsInCart.add(wingToBeSavedToCart);
                }
                Collections.sort(currentListOfWingsInCart, Comparator.comparing(Wing::getWingId));
                Cart cartToSave = queriedCartList.get(0);
                mongoTemplate.save(cartToSave);
                int currentWingOrderInCart = queriedCartList.get(0).getWings().size();
                sumOfWingAndPizzaInCart = totalPizzaOrderInCart + currentWingOrderInCart;
            }
            //list of wing is empty for this user's cart
            else {
                //take existing cart and add pizza if existing cart exist
                Cart cartToSave = queriedCartList.get(0);
                cartToSave.setWings(cart.getWings());
                mongoTemplate.save(cartToSave);
                sumOfWingAndPizzaInCart = 1 + totalPizzaOrderInCart;//only 1 wing to save
            }
        }
        //user has no cart
        else {
            mongoTemplate.save(cart);
            sumOfWingAndPizzaInCart = 1;
        }
        return sumOfWingAndPizzaInCart;
    }

    @Override
    public Cart update(UpdateItemFromCartRequest request) throws PizzeriaException {
        int userId = Integer.parseInt(this.helperBean.decrypt(request.getEnc()));
        Cart currentCart = this.get(userId);
        String type = request.getType().trim();
        if(currentCart != null) {
            if(type.equals(Constant.WING_TYPE)) {
                List<Wing> wings = currentCart.getWings();
                if(!wings.isEmpty()) {
                    int wingIdToUpdate = request.getWing().getWingId();
                    int originalSelectedQty = request.getOriginalSelectedQty();
                    String originalSelectedFlavor = request.getOriginalSelectedFlavor();
                    int originalNumberOfOrder = request.getOriginalNumberOfOrder();
                    Optional<Wing> answer;
                    //wing w/out flavor
                    if(originalSelectedFlavor == null) {
                        answer = wings.stream().filter(wing -> (wing.getWingId() == wingIdToUpdate
                                && wing.getSelectedQty() == originalSelectedQty
                                && wing.getNumberOfOrder() == originalNumberOfOrder)).findAny();
                    } else {
                        answer = wings.stream().filter(wing -> (wing.getWingId() == wingIdToUpdate
                                && wing.getSelectedQty() == originalSelectedQty
                                && wing.getSelectedFlavor().equals(originalSelectedFlavor)
                                && wing.getNumberOfOrder() == originalNumberOfOrder)).findAny();
                    }
                    if(answer.isPresent()) {
                        answer.get().setNumberOfOrder(request.getWing().getNumberOfOrder());
                        answer.get().setSelectedQty(request.getWing().getSelectedQty());
                        answer.get().setSelectedPrice(request.getWing().getSelectedPrice());
                        if(request.getWing().getSelectedFlavor() != null) {
                            answer.get().setSelectedFlavor(request.getWing().getSelectedFlavor());
                        }
                        Cart updatedCart = this.updateCart(currentCart);
                        return updatedCart;
                    } else {
                        throw new PizzeriaException("item to be updated is not in your cart", 500);
                    }
                } else {
                    throw new PizzeriaException("item to be updated is not in your cart", 500);
                }
            } else {
                //TODO: pizza type
                return null;
            }
        } else {
            log.error("unable to find cart for user id: " + userId);
            throw new PizzeriaException("cart not available", 200);
        }
    }

    @Override
    public Cart get(int userId) throws PizzeriaException {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<Cart> cart = this.mongoTemplate.find(query, Cart.class);
        if(cart.size() > 0) {
            return cart.get(0);//should always be 1 per userId
        } else {
            return null;
        }

    }

    @Override
    public Cart remove(RemoveItemFromCartRequest request) throws PizzeriaException {
        int userId = Integer.parseInt(this.helperBean.decrypt(request.getEnc()));
        Cart currentCart = this.get(userId);
        String type = request.getType().trim();
        if(currentCart != null) {
            if(type.equals(Constant.WING_TYPE)) {
                List<Wing> wings = currentCart.getWings();
                boolean isRemoveSuccess = wings.removeIf(wing -> {
                    boolean isMatch = wing.equals(request.getWing());
                    return isMatch;
                });
                if(isRemoveSuccess) {
                    return this.updateCart(currentCart);
                }
                throw new PizzeriaException("item to be removed is not in your cart", 500);
            } else {
                //TODO: remove pizza type
                return null;
            }
        } else {
            log.error("unable to find cart for user id: " + userId);
            throw new PizzeriaException("cart not available", 200);
        }
    }

    private Cart updateCart(Cart cart) {
        Cart updatedCart = this.mongoTemplate.save(cart);
        return updatedCart;
    }
}
