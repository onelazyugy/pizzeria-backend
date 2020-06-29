package com.vietle.pizzeria.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "user_sequence")
public class MongoUserSequence {
    @Id
    private String id;
    private int seq;
}
