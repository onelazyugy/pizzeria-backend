package com.vietle.pizzeria.controller;

import com.vietle.pizzeria.domain.request.LoginUserRequest;
import com.vietle.pizzeria.domain.request.RegisterUserRequest;
import com.vietle.pizzeria.domain.response.LoginUserResponse;
import com.vietle.pizzeria.domain.response.RegisterUserResponse;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.service.UserService;
import com.vietle.pizzeria.util.Validation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest request) throws PizzeriaException {
//        throw new PizzeriaException("unable to register user", 400);
        log.info("/register: " + request.getEmail());
        Validation.validateUserRegistrationInfo(request);
        RegisterUserResponse response = this.userService.register(request);
        ResponseEntity<RegisterUserResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        log.info("/register response: " + responseEntity.getBody());
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest request) throws PizzeriaException {
        log.info("/login: " + request.getEmail());
        Validation.validateUserLoginInfo(request);
        LoginUserResponse loginResponse = this.userService.login(request);
        ResponseEntity<LoginUserResponse> responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        log.info("/login response: " + responseEntity.getBody());
        return responseEntity;
    }

    @GetMapping("/topsecret")
    public String whoami(@RequestParam(required=false, defaultValue="no name") String name) {
        log.info("REQUEST: " + name);
        String q1 = "\"";
        return "{\"name\": " +  q1 + name +  q1 + "}";
    }
}

