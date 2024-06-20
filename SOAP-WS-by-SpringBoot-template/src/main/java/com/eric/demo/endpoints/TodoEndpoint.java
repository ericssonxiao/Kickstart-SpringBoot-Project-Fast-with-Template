package com.eric.demo.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.eric.demo.repository.TodoRepository;
import com.eric.xml.todoapp.TodoInfoRequest;
import com.eric.xml.todoapp.TodoInfoResponse;

@Endpoint
public class TodoEndpoint {
    
    private static final String NAMESPACE_URI = "http://www.eric.com/xml/todoapp";

    private TodoRepository todoRepository;

    @Autowired
    public TodoEndpoint(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TodoInfoRequest")
    @ResponsePayload
    public TodoInfoResponse getTodo(@RequestPayload TodoInfoRequest todoInfoRequest){
        TodoInfoResponse response = new TodoInfoResponse();
        response.setTodo(null);
        return response;
    }

}
