package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepositoryJPA extends CrudRepository<UserEntity, Long> {

}
