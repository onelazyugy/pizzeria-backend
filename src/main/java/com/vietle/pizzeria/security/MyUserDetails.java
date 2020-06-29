package com.vietle.pizzeria.security;

import com.vietle.pizzeria.domain.User;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {
    private static Logger LOG = LoggerFactory.getLogger(MyUserDetails.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User foundUser = null;
        try {
            foundUser = this.userRepository.retrieve(email);
            if (foundUser == null) {
                throw new UsernameNotFoundException("Email '" + email + "' not found");
            }

        } catch (PizzeriaException ee) {
            LOG.error("error finding user");//TODO:
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
