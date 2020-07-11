package com.vietle.pizzeria.domain.request;

import com.vietle.pizzeria.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPizzaToCartRequest {
    private String selectedPizzaSize;
    private String orderType;
    private String img;
//    private BigDecimal selectedPizzaPrice;
    private List<Ingredient> selectedCheese;
    private List<Ingredient> selectedMeat;
    private List<Ingredient> selectedVeggie;
    private String enc;
    private int userId;
}
