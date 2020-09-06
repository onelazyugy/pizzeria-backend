package com.vietle.pizzeria.repo;

import com.vietle.pizzeria.domain.User;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.service.MongoUserSequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * this is DEPRECATED since saving user info will be handle by postgres
 */
@Component
@Qualifier("userRepositoryImplMongo")
public class UserRepositoryImplMongo implements UserRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoUserSequenceService mongoUserSequenceService;
    private static Logger LOG = LoggerFactory.getLogger(UserRepositoryImplMongo.class);

    @Override
    public boolean save(User user) throws PizzeriaException {
//        int nextSequence = mongoUserSequenceService.getNextSequence("sequence");
//        user.setId(nextSequence);
//        user.setPassword(PizzeriaUtil.hash(user.getPassword()));
//        user.setConfirmPassword(null);
//
//        List<Role> roleList = new ArrayList<>(1);
//        roleList.add(Role.ROLE_USER); // hard code for now, need a UI in the admin screen to set role
//        user.setRoles(roleList); // hard code for now, need a UI in the admin screen to set role
//        // TODO: check if email already exist before saving
//        User savedUser = mongoTemplate.save(user);
//        if(savedUser != null) {
//            return true;
//        }
//        return false;
        return false;
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
//        Query query = new Query();
//        query.addCriteria(Criteria.where("email").is(email.trim()));
//        List<User> foundUsers = this.mongoTemplate.find(query, User.class);
//        User foundUser = foundUsers.stream().findFirst().orElseThrow(() -> new PizzeriaException("invalid login!", 400));
//        return foundUser;
        return null;
    }

    @Override
    public User retrieve(User user) throws PizzeriaException {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("email").is(user.getEmail().trim()));
//        query.addCriteria(Criteria.where("password").is(PizzeriaUtil.hash(user.getPassword().trim())));
//        List<User> foundUsers = this.mongoTemplate.find(query, User.class);
//        User foundUser = foundUsers.stream().findFirst().orElseThrow(() -> new PizzeriaException("invalid login!", 400));
//        foundUser.setPassword(null);
//        return foundUser;

        return null;
    }
}

