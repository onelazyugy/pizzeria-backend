package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.User;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.util.PizzeriaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Qualifier("userRepositoryImplPostGres")
@Slf4j
public class UserRepositoryImplPostGres implements UserRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImplPostGres(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(User user) throws PizzeriaException {
        try {
            String hashedPassword = PizzeriaUtil.hash(user.getPassword());
            user.setPassword(hashedPassword);
            String query = QueryBuilder.buildSaveUserQuery(user);
            log.info(String.format("SAVE USER QUERY: %s", query));
            this.jdbcTemplate.execute(query);
            return true;
        } catch (DataAccessException dae) {
            log.error(String.format("REGISTER USER ERROR: %s", dae.getMessage()));
            throw new PizzeriaException("unable to register user", 500);
        } catch (Exception e) {
            log.error(String.format("REGISTER USER ERROR: %s", e.getMessage()));
            throw new PizzeriaException("password does not match", 500);
        }
    }

    @Override
    public boolean delete(int id) throws PizzeriaException {
        return false;
    }

    @Override
    public boolean update(User user) throws PizzeriaException {
        return false;
    }

    @Override
    public User retrieve(int id) throws PizzeriaException {
        return null;
    }

    @Override
    public User retrieve(String email) throws PizzeriaException {
        try {
            String retrieveUserQuery = QueryBuilder.buildRetrieveUserQuery(email);
            log.info(String.format("RETRIEVE USER BY EMAIL QUERY: %s", retrieveUserQuery));
            User retrievedUser = this.jdbcTemplate.queryForObject(retrieveUserQuery, new UserRowMapper());
            return retrievedUser;
        } catch (DataAccessException dae) {
            log.error(String.format("RETRIEVE USER BY EMAIL ERROR: %s", dae.getMessage()));
            throw new PizzeriaException("unable to register user", 500);
        } catch (Exception e) {
            log.error(String.format("RETRIEVE USER BY EMAIL ERROR: %s", e.getMessage()));
            throw new PizzeriaException("password does not match", 500);
        }
    }

    @Override
    public User retrieve(User user) throws PizzeriaException {
        try {
           String retrieveUserQuery = QueryBuilder.buildRetrieveUserQuery(user);
           log.info(String.format("LOGIN USER QUERY: %s", retrieveUserQuery));
           User retrievedUser = this.jdbcTemplate.queryForObject(retrieveUserQuery, new UserRowMapper());
           return retrievedUser;
        } catch (DataAccessException dae) {
            log.error(String.format("LOGIN USER ERROR: %s", dae.getMessage()));
            throw new PizzeriaException("unable to register user", 500);
        } catch (Exception e) {
            log.error(String.format("LOGIN USER ERROR: %s", e.getMessage()));
            throw new PizzeriaException("password does not match", 500);
        }
    }
}
