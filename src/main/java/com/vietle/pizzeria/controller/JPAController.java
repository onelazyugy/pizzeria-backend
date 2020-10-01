package com.vietle.pizzeria.controller;

import com.vietle.pizzeria.entity.UserEntity;
import com.vietle.pizzeria.repo.UserRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/jpa")
@Slf4j
public class JPAController {
    @Autowired
    private UserRepositoryJPA userRepositoryJPA;


    @GetMapping("/user")
    public ResponseEntity<Object> findUserById(@RequestParam Long userId) {
        log.info(String.format("userId: %s", userId));
        Optional<UserEntity> optionalUserEntity = userRepositoryJPA.findById(userId);
        UserEntity userEntity = optionalUserEntity.get();
        return null;
    }
}
