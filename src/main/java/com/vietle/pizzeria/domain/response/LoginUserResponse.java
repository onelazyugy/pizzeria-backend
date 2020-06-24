package com.vietle.pizzeria.domain.response;

import com.vietle.pizzeria.domain.Status;
import com.vietle.pizzeria.domain.Token;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginUserResponse extends Response{
    private boolean success;
    private String email;
    private int id;
    private Token token;
    private String nickName;

    @Builder
    public LoginUserResponse(boolean success, String email, int id, Token token, String nickName, Status status) {
        super(status);
        this.success = success;
        this.email = email;
        this.id = id;
        this.token = token;
        this.nickName = nickName;
    }
}
