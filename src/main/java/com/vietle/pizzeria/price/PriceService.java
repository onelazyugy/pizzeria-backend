package com.vietle.pizzeria.price;

import com.vietle.pizzeria.constant.Constant;
import com.vietle.pizzeria.domain.*;
import com.vietle.pizzeria.exception.PizzeriaException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PriceService {
    private static BigDecimal PIZZA_LARGE_PRICE = new BigDecimal(10.99);
    private static BigDecimal PIZZA_MEDIUM_PRICE = new BigDecimal(8.99);
    private static BigDecimal PIZZA_SMALL_PRICE = new BigDecimal(6.99);
    private static BigDecimal MEAT_TOPIC_PRICE = new BigDecimal(.70);
    private static BigDecimal CHEESE_TOPIC_PRICE = new BigDecimal(.50);
    private static BigDecimal VEGGIE_TOPIC_PRICE = new BigDecimal(.60);
    private static BigDecimal TAX_PERCENTAGE = new BigDecimal(0.07);


    public CartSummary calculateSummary(Cart cart) throws PizzeriaException {
        BigDecimal totalPriceForAllPizzas = this.totalPriceForAllPizzas(cart.getPizzas());
        BigDecimal totalPirceForAllWings = this.totalPriceForAllWings(cart.getWings());
        CartSummary cartSummary = this.calculateCartSummary(totalPriceForAllPizzas, totalPirceForAllWings);
        return cartSummary;
    }

    private BigDecimal totalPriceForAllPizzas(List<Pizza> pizzas) throws PizzeriaException {
        BigDecimal totalPizzaPriceForAllPizzas = BigDecimal.ZERO;
        if(pizzas != null && !pizzas.isEmpty()) {
            BigDecimal pizzaPriceBasedOnSizeForCurrentPizza = BigDecimal.ZERO;
            BigDecimal totalPriceForMeatForAllPizza = BigDecimal.ZERO;
            BigDecimal totalPriceForCheeseForAllPizza = BigDecimal.ZERO;
            BigDecimal totalPriceForVeggieForAllPizza = BigDecimal.ZERO;
            for(Pizza pizza:pizzas) {
                pizzaPriceBasedOnSizeForCurrentPizza = pizzaPriceBasedOnSizeForCurrentPizza.add(this.getPizzaPriceBasedOnSize(pizza.getSelectedPizzaSize()));
                List<Ingredient> meats = pizza.getSelectedMeat();
                List<Ingredient> cheeses = pizza.getSelectedCheese();
                List<Ingredient> veggies = pizza.getSelectedVeggie();
                if(!meats.isEmpty()) {
                    BigDecimal totalMeatToppingPriceForCurrentPizza = MEAT_TOPIC_PRICE.multiply(new BigDecimal(meats.size()));
                    totalPriceForMeatForAllPizza = totalPriceForMeatForAllPizza.add(totalMeatToppingPriceForCurrentPizza);
                }
                if(!cheeses.isEmpty()) {
                    BigDecimal totalCheeseToppingPriceForCurrentPizza = CHEESE_TOPIC_PRICE.multiply(new BigDecimal(cheeses.size()));
                    totalPriceForCheeseForAllPizza = totalPriceForCheeseForAllPizza.add(totalCheeseToppingPriceForCurrentPizza);
                }
                if(!veggies.isEmpty()) {
                    BigDecimal totalVeggieToppingPriceForCurrentPizza = VEGGIE_TOPIC_PRICE.multiply(new BigDecimal(veggies.size()));
                    totalPriceForVeggieForAllPizza = totalPriceForVeggieForAllPizza.add(totalVeggieToppingPriceForCurrentPizza);
                }
            }
            totalPizzaPriceForAllPizzas = totalPizzaPriceForAllPizzas.add(pizzaPriceBasedOnSizeForCurrentPizza).add(totalPriceForMeatForAllPizza)
                    .add(totalPriceForCheeseForAllPizza).add(totalPriceForVeggieForAllPizza);
            return totalPizzaPriceForAllPizzas;
        } else {
            return null;
        }
    }

    private BigDecimal totalPriceForAllWings(List<Wing> wings) throws PizzeriaException {
        if(wings != null && !wings.isEmpty()) {
            BigDecimal totalPriceForAllWings = BigDecimal.ZERO;
            for(Wing wing: wings) {
                BigDecimal price = wing.getSelectedPrice();
                int numberOfOrder = wing.getNumberOfOrder();
                BigDecimal priceForCurrentWing = new BigDecimal(numberOfOrder).multiply(price);
                totalPriceForAllWings = totalPriceForAllWings.add(priceForCurrentWing);
            }
            return totalPriceForAllWings;
        } else {
            return null;
        }
    }

    public BigDecimal getPizzaPriceBasedOnSize(String size) throws PizzeriaException {
        if(StringUtils.isEmpty(size)) {
            throw new PizzeriaException("pizza size is not available", 400);
        }
        switch (size) {
            case Constant.SMALL:
                return PIZZA_SMALL_PRICE;
            case Constant.MEDIUM:
                return PIZZA_MEDIUM_PRICE;
            case Constant.LARGE:
                return PIZZA_LARGE_PRICE;
            default:
                throw new PizzeriaException("Unable to determine pizza size!", 400);
        }
    }

    private CartSummary calculateCartSummary(BigDecimal totalPriceForAllPizzas, BigDecimal totalPriceForAllWings) {
        BigDecimal tempPizzaPrice = BigDecimal.ZERO;
        BigDecimal tempWingPrice = BigDecimal.ZERO;
        //this is bc wing or pizza could be empty
        if(totalPriceForAllWings != null) {
            tempWingPrice = totalPriceForAllWings;
        }
        if(totalPriceForAllPizzas != null) {
            tempPizzaPrice = totalPriceForAllPizzas;
        }
        BigDecimal subTotal = tempWingPrice.add(tempPizzaPrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = subTotal.multiply(TAX_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subTotal.add(tax).setScale(2, RoundingMode.HALF_UP);
        CartSummary cartSummary = CartSummary.builder().subTotal(subTotal).tax(tax).total(total).build();
        return cartSummary;
    }
}
