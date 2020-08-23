package com.vietle.pizzeria.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wing {
    private int wingId;
    private String name;
    private String desc;
    private String img;
    private BigDecimal selectedPrice;
    private int selectedQty;
    private String selectedFlavor;
    private int numberOfOrder;
    private boolean hasFlavor;

    // don't compare numberOfOrder property
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Wing wing = (Wing) object;
        return wingId == wing.wingId &&
                selectedQty == wing.selectedQty &&
                hasFlavor == wing.hasFlavor &&
                Objects.equals(name, wing.name) &&
                Objects.equals(desc, wing.desc) &&
                Objects.equals(img, wing.img) &&
                Objects.equals(selectedPrice, wing.selectedPrice) &&
                Objects.equals(selectedFlavor, wing.selectedFlavor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wingId, name, desc, img, selectedPrice, selectedQty, selectedFlavor, hasFlavor);
    }
}
