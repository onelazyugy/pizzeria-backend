package com.vietle.pizzeria.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "cart_sequence")
public class MongoCartSequence {
    @Id
    private String id;
    private int seq;
}
