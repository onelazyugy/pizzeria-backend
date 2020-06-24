package com.vietle.pizzeria.controller;

import com.vietle.pizzeria.domain.request.LoginUserRequest;
import com.vietle.pizzeria.domain.request.RegisterUserRequest;
import com.vietle.pizzeria.domain.response.LoginUserResponse;
import com.vietle.pizzeria.domain.response.RegisterUserResponse;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.service.UserService;
import com.vietle.pizzeria.util.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest request) throws PizzeriaException {
//        throw new PizzeriaException("unable to register user", 400);
        LOG.info("/register: " + request.getEmail());
        Validation.validateUserRegistrationInfo(request);
        RegisterUserResponse response = this.userService.register(request);
        ResponseEntity<RegisterUserResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        LOG.info("/register response: " + responseEntity.getBody());
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest request) throws PizzeriaException {
        LOG.info("/login: " + request.getEmail());
        Validation.validateUserLoginInfo(request);
        LoginUserResponse loginResponse = this.userService.login(request);
        ResponseEntity<LoginUserResponse> responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        LOG.info("/login response: " + responseEntity.getBody());
        return responseEntity;
    }

    @GetMapping("/topsecret")
    public String whoami(@RequestParam(required=false, defaultValue="no name") String name) {
        LOG.info("REQUEST: " + name);
        String q1 = "\"";
        return "{\"name\": " +  q1 + name +  q1 + "}";
    }

//    @GetMapping("/test")
//    public String getPizza() {
//        String pizza = "I am a delicious pizza";
//        String q1 = "\"";
//        return "{\"pizza\": " +  q1 + pizza +  q1 + "}";
//    }
}

