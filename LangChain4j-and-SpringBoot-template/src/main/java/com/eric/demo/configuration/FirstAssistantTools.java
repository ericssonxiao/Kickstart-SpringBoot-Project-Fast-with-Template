package com.eric.demo.configuration;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

import dev.langchain4j.agent.tool.Tool;

@Component
public class FirstAssistantTools {

    @Tool("Get Current Time")
    String currentTime(){
        return LocalTime.now().toString();
    }
}
