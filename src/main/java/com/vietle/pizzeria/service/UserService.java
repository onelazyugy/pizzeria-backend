package com.vietle.pizzeria.service;

import com.vietle.pizzeria.Constant;
import com.vietle.pizzeria.domain.Status;
import com.vietle.pizzeria.domain.Token;
import com.vietle.pizzeria.domain.User;
import com.vietle.pizzeria.domain.request.LoginUserRequest;
import com.vietle.pizzeria.domain.request.RegisterUserRequest;
import com.vietle.pizzeria.domain.response.LoginUserResponse;
import com.vietle.pizzeria.domain.response.RegisterUserResponse;
import com.vietle.pizzeria.exception.PizzeriaException;
import com.vietle.pizzeria.repo.UserRepository;
import com.vietle.pizzeria.security.JwtHelper;
import com.vietle.pizzeria.util.PizzeriaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private static Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserRepository userRepository;

    public LoginUserResponse login(LoginUserRequest loginUserRequest) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        User user = User.builder().email(loginUserRequest.getEmail()).password(loginUserRequest.getPassword()).build();
        User retrievedUser = this.userRepository.retrieve(user);
        String token = jwtHelper.createToken(retrievedUser.getEmail(), retrievedUser.getRoles());
        Token accessToken = Token.builder().accessToken(token).build();
        Status status = Status.builder().statusCd(200).message(Constant.SUCCESS).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
        LoginUserResponse loginUserResponse = LoginUserResponse.builder().status(status).email(user.getEmail()).id(retrievedUser.getId()).nickName(retrievedUser.getNickName()).success(true).token(accessToken).build();
        return loginUserResponse;
    }

    public RegisterUserResponse register(RegisterUserRequest registerUserRequest) throws PizzeriaException {
        String transactionId = UUID.randomUUID().toString();
        User user = User.builder().nickName(registerUserRequest.getNickName()).email(registerUserRequest.getEmail()).password(registerUserRequest.getPassword()).confirmPassword(registerUserRequest.getConfirmPassword()).build();
        boolean isSavedSuccess = this.userRepository.save(user);
        if(isSavedSuccess) {
            Status status = Status.builder().statusCd(200).message(Constant.SUCCESS).transactionId(transactionId).timestamp(PizzeriaUtil.getTimestamp()).build();
            RegisterUserResponse registerUserResponse = RegisterUserResponse.builder().email(user.getEmail()).success(true).status(status).build();
            return registerUserResponse;
        }
        throw new PizzeriaException("unable to register user " + user.getEmail(), 500);
    }

//    public UserResponse findUserById(int id) throws EcommerceException{
//        String transactionId = UUID.randomUUID().toString();
//        User retrievedUser = this.userRepository.retrieve(id);
//        Status status = Status.builder().statusCd(200).message(Constant.SUCCESS).transactionId(transactionId).timestamp(EcommerceUtil.getTimestamp()).build();
//        //TODO: retrievedUser
//        UserResponse response = UserResponse.builder().user(retrievedUser).status(status).token(null).build();
//        return response;
//    }
}
