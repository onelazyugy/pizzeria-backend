package com.vietle.pizzeria.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document(collection = "cart")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cart {
    @Id
    private int id;
    private int userId;
    private List<Wing> wings;
    private List<Pizza> pizzas;
}
