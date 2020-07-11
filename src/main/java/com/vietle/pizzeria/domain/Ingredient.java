package com.vietle.pizzeria.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
    private String type;
    private int id;
    private String name;
    private @JsonProperty("isSelected") boolean isSelected;
    private String image;
}
