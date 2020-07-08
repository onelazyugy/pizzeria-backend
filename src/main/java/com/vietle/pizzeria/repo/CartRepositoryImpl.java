package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.constant.Constant;
import com.vietle.pizzeria.domain.Cart;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        int nextSequence = mongCartSequenceService.getNextSequence("sequence");
        cart.setId(nextSequence);
        if(cart.getWings().size() > 0) {
            return saveWingOrderToCart(cart);
        } else {
            //save pizza order to cart
            return 0;
        }
    }

    private int saveWingOrderToCart(Cart cart) {
        int totalWingOrderInCart;
        //save wing order to cart
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(cart.getUserId()));
        //guaranteed to be 1 cart per userId
        List<Cart> queriedCartList = this.mongoTemplate.find(query, Cart.class);
        if(queriedCartList != null && queriedCartList.size() > 0) {
            Wing wingToBeSaveToCart = cart.getWings().get(0);
            List<Wing> currentListOfWingsInCart = queriedCartList.get(0).getWings();
            Collections.sort(currentListOfWingsInCart, Comparator.comparing(Wing::getWingId));
            boolean matched = false;
            for(Wing wing: currentListOfWingsInCart) {
                // wing with flavor option
                if(wing.getWingId() == wingToBeSaveToCart.getWingId() &&
                        wing.isHasFlavor() && wingToBeSaveToCart.isHasFlavor() &&
                        wing.getSelectedQty() == wingToBeSaveToCart.getSelectedQty() &&
                        wing.getSelectedFlavor().equals(wingToBeSaveToCart.getSelectedFlavor()) &&
                        wing.getSelectedPrice().compareTo(wingToBeSaveToCart.getSelectedPrice()) == 0) {
                    int currentOrderCount = wing.getNumberOfOrder();
                    wing.setNumberOfOrder(currentOrderCount+1);
                    matched = true;
                }
                // wing with no flavor option
                else if(!wingToBeSaveToCart.isHasFlavor() && !wing.isHasFlavor() && wing.getWingId() == wingToBeSaveToCart.getWingId() &&
                        wing.getSelectedPrice().compareTo(wingToBeSaveToCart.getSelectedPrice()) == 0 && wing.getSelectedQty() == wingToBeSaveToCart.getSelectedQty()) {
                    int currentOrderCount = wing.getNumberOfOrder();
                    wing.setNumberOfOrder(currentOrderCount+1);
                    matched = true;
                }
            }
            if(!matched){
                currentListOfWingsInCart.add(wingToBeSaveToCart);
            }
            Collections.sort(currentListOfWingsInCart, Comparator.comparing(Wing::getWingId));
            Cart cartToSave = queriedCartList.get(0);
            mongoTemplate.save(cartToSave);
            totalWingOrderInCart = queriedCartList.get(0).getWings().size();
        } else {
            mongoTemplate.save(cart);
            totalWingOrderInCart = 1;
        }
        return totalWingOrderInCart;
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
                int wingId = request.getItemId();
                int numberOfOrder = request.getNumberOfOrder();
                boolean success = wings.removeIf(wing->(wing.getWingId()==wingId && wing.getNumberOfOrder() == numberOfOrder));
                if(success && wings.size() > 0) {
                    Cart updatedCart = this.updateCart(currentCart);
                    return updatedCart;
                } else if(success && wings.size() == 0) {
                    Cart emptyCart = Cart.builder().userId(userId).wings(new ArrayList<>()).id(currentCart.getId()).build();
                    this.updateCart(emptyCart);
                    return emptyCart;
                } else {
                    throw new PizzeriaException("item to be removed is not in your cart", 500);
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

    private Cart updateCart(Cart cart) {
        Cart updatedCart = this.mongoTemplate.save(cart);
        return updatedCart;
    }
}
