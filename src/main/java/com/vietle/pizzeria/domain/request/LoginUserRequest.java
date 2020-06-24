package com.vietle.pizzeria.domain.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {
    private String email;
    private String password;
}
