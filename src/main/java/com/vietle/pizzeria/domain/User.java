package com.vietle.pizzeria.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "user")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    private int id;
    private String email;
    private String password;
    private String confirmPassword;
    private String nickName;
    private String signupDate;
    private List<Role> roles;
}