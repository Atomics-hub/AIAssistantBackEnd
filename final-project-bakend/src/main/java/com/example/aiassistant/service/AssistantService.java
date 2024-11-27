package com.example.aiassistant.service;

import com.example.aiassistant.dto.*;
import com.example.aiassistant.model.*;
import com.example.aiassistant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssistantService {

    private final AssistantConversation assistantConversation;
    private final List<String> assistantList;

    @Autowired
    public AssistantService(AssistantConversation assistantConversation) {
        this.assistantConversation = assistantConversation;
        this.assistantList = new ArrayList<>();
    }

    // Create Assistant
    public String createAssistant(String assistantName, boolean verbose) {
        assistantConversation.createAssistant(assistantName, verbose);
        assistantList.add(assistantConversation.getAssistantId());
        return assistantConversation.getAssistantId();
    }

    public void addAssistant(String assistantId) {
        assistantList.add(assistantId);
    }

    public String createUserMessage(String message) {
        return assistantConversation.createUserMessage(assistantConversation.threadId, message);
    }

    public String assistantReply(String message, String threadId, String assistantId) {
        assistantConversation.createUserMessage(assistantConversation.threadId, message);
        return assistantConversation.assistantReply(threadId, assistantId);
    }

    public String getThreadId() {
        return assistantConversation.getThreadId();
    }

    public String getAssistantId() {
        return assistantConversation.getAssistantId();
    }

    public ArrayList<String> getChatMessages() {
        return assistantConversation.getChatMessages();
    }

    public boolean uploadFileToAssistant(String filePath, boolean verbose) {
        return assistantConversation.uploadFileToAssistant(filePath, verbose);
    }

    public String getAssistantList() {
        String result = "[";
        for (String assistantId : assistantList) {
            result = result + assistantId + ", ";
        }
        return result;
    }
}
