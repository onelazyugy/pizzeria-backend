package com.vietle.pizzeria.domain.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String nickName;
}
