package com.vietle.pizzeria.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vietle.pizzeria.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Status status;
}
