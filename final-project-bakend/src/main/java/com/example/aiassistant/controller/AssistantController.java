package com.example.aiassistant.controller;

import com.example.aiassistant.dto.*;
import com.example.aiassistant.service.AssistantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AssistantController {
    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @PostMapping("/login")
    public UserSessionDTO login() {
        return assistantService.createUserSession();
    }

    @GetMapping("/assistants")
    public List<AssistantDTO> getAllAssistants() {
        return assistantService.getAllAssistants();
    }

    @PostMapping("/assistants")
    public AssistantDTO createAssistant(@RequestBody AssistantDTO request) {
        return assistantService.createAssistant(request);
    }

    @GetMapping("/assistants/{assistantId}/chats")
    public List<ChatDTO> getChats(
            @PathVariable String assistantId,
            @RequestHeader("User-Id") String userId) {
        return assistantService.getChats(assistantId, userId);
    }

    @PostMapping("/assistants/{assistantId}/chats")
    public ChatDTO createChat(
            @PathVariable String assistantId,
            @RequestHeader("User-Id") String userId) {
        return assistantService.createChat(assistantId, userId);
    }

    @GetMapping("/chats/{chatId}/messages")
    public List<MessageDTO> getMessages(
            @PathVariable String chatId,
            @RequestHeader("User-Id") String userId) {
        return assistantService.getMessages(chatId, userId);
    }

    @PostMapping("/chats/{chatId}/messages")
    public MessageDTO addMessage(
            @PathVariable String chatId,
            @RequestHeader("User-Id") String userId,
            @RequestBody MessageDTO message) {
        return assistantService.addMessage(chatId, userId, message);
    }
}