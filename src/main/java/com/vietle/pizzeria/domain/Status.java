package com.vietle.pizzeria.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Status {
    private String timestamp;
    private String message;
    private String transactionId;
    private int statusCd;
}
