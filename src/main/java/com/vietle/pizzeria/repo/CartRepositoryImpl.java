package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.Cart;
import com.vietle.pizzeria.domain.Wing;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.service.MongCartSequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class CartRepositoryImpl implements CartRepository {
    private static Logger LOG = LoggerFactory.getLogger(CartRepository.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongCartSequenceService mongCartSequenceService;

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
    public boolean delete(Object object) throws PizzeriaException {
        return false;
    }

    @Override
    public boolean update(Object object) throws PizzeriaException {
        return false;
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
}
