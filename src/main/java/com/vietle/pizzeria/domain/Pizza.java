package com.vietle.pizzeria.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pizza {
    private int pizzaId;
    private String orderType;
    private String img;
    private BigDecimal selectedPizzaPrice;
    private String selectedPizzaSize;
    private List<Ingredient> selectedCheese;
    private List<Ingredient> selectedMeat;
    private List<Ingredient> selectedVeggie;
    private int numberOfOrder;

    // don't compare numberOfOrder property
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Pizza pizza = (Pizza) object;
        return pizzaId == pizza.pizzaId &&
                Objects.equals(orderType, pizza.orderType) &&
                Objects.equals(img, pizza.img) &&
                Objects.equals(selectedPizzaPrice, pizza.selectedPizzaPrice) &&
                Objects.equals(selectedPizzaSize, pizza.selectedPizzaSize) &&
                Objects.equals(selectedCheese, pizza.selectedCheese) &&
                Objects.equals(selectedMeat, pizza.selectedMeat) &&
                Objects.equals(selectedVeggie, pizza.selectedVeggie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pizzaId, orderType, img, selectedPizzaPrice, selectedPizzaSize, selectedCheese, selectedMeat, selectedVeggie);
    }
}
