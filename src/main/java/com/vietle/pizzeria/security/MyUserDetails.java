package com.vietle.pizzeria.security;

import com.vietle.pizzeria.domain.User;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyUserDetails implements UserDetailsService {
    //postgres
    @Autowired
    @Qualifier("userRepositoryImplPostGres")
    private UserRepository userRepository;

    // {"_id":{"$numberInt":"4"},"email":"viet@gmail.com","password":"209400a93168b367e9604f427bb1c5a56f546889262014e84f153a1d273cf631","nickName":"VIET","roles":["ROLE_USER"],"_class":"com.vietle.pizzeria.domain.User"}
    //mongo
//    @Autowired
//    @Qualifier("userRepositoryImplMongo")
//    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User foundUser = null;
        try {
            foundUser = this.userRepository.retrieve(email);
            if (foundUser == null) {
                throw new UsernameNotFoundException("Email '" + email + "' not found");
            }

        } catch (PizzeriaException ee) {
            log.error("error finding user");//TODO:
        }
        return org.springframework.security.core.userdetails.User//
                .withUsername(email)
                .password(foundUser.getPassword())
                .authorities(foundUser.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
