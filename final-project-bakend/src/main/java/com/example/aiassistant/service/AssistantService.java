package com.example.aiassistant.service;

import com.example.aiassistant.dto.*;
import com.example.aiassistant.model.*;
import com.example.aiassistant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssistantService {

    private final AssistantConversation assistantConversation;


    @Autowired
    public AssistantService(AssistantConversation assistantConversation) {
        this.assistantConversation = assistantConversation;
    }

    // Create Assistant
    public String createAssistant(String assistantName, boolean verbose) {
        assistantConversation.createAssistant(assistantName, verbose);
        return assistantConversation.getAssistantId();
    }

    public String createUserMessage(String message) {
        return assistantConversation.createUserMessage(assistantConversation.threadId, message);
    }

    public String assistantReply(String threadId, String assistantId) {
        return assistantConversation.assistantReply(threadId, assistantId);
    }

    public String getThreadId() {
        return assistantConversation.getThreadId();
    }

    public String getAssistantId() {
        return assistantConversation.getAssistantId();
    }

}
