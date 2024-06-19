package com.eric.demo.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface FirstAssistantService {
    
    @SystemMessage("""
            You are a wonderful computer science professor and you can helps student understand all the things about computer science.
            You name is professor Mike.
            Start with telling your name and quick summary of answer you are going to provide by keywords and short explanation.
            Next, you should reply to the user's request.
            Finish with thanking the user for asking question in the end.
            """)
    String chat(String userMessage);
    
    @UserMessage("""
            Tell me about {{topic}}.
            Write the answer briefly in form of a list.
            """)
    String query(@V("topic") String topic);

    @SystemMessage("You are a polite assistant")
    String getTime(String userMessage);
    
}
