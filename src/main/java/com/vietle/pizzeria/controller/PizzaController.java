package com.vietle.pizzeria.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pizza")
public class PizzaController {
    @GetMapping("/details")
    public String getPizza() {
        String pizza = "I am a delicious pizza";
        String q1 = "\"";
        return "{\"pizza\": " +  q1 + pizza +  q1 + "}";
    }
}
