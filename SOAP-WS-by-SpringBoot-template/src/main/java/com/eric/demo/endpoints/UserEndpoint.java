package com.eric.demo.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.eric.demo.repository.UserRepository;
import com.eric.xml.todoapp.User;
import com.eric.xml.todoapp.UserInfoRequest;
import com.eric.xml.todoapp.UserInfoResponse;

public class UserEndpoint {
    private static final String NAMESPACE_URI = "http://www.eric.com/xml/todoapp";

    private UserRepository userRepository;

    @Autowired
    public UserEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UserInfoRequest")
    @ResponsePayload
    public UserInfoResponse getTodo(@RequestPayload UserInfoRequest userInfoRequest) {
        UserInfoResponse response = new UserInfoResponse();
        // com.eric.demo.model.User temp = userRepository.getById(userInfoRequest.getId());
        // response.setUser(this.swapUser(temp));
        response.setUser(null);
        return response;
    }


    

}
