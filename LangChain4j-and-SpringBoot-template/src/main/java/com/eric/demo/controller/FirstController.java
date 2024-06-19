package com.eric.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.eric.demo.service.FirstAssistantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class FirstController {
    
    private FirstAssistantService firstAssistantService;

    public FirstController(FirstAssistantService firstAssistantService) {
        this.firstAssistantService = firstAssistantService;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return firstAssistantService.chat(message);
    }
    

    @GetMapping("/query")
    public String query(@RequestParam String topic) {
        return firstAssistantService.chat(topic);
    }
    
    @GetMapping("/getTime")
    public String getTime(@RequestParam(value="message", defaultValue = "What is the time now?") String message) {
        return firstAssistantService.getTime(message);
    }
    
    

}
