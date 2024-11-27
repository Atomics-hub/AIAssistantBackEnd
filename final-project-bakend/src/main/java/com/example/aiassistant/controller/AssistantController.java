package com.example.aiassistant.controller;

import com.example.aiassistant.dto.*;
import com.example.aiassistant.service.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AssistantController {

    private final AssistantService assistantService;

    @Autowired
    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    // Create Assistant
    @PostMapping("/createAssistant")
    public ResponseEntity<String> createAssistant(@RequestBody Map<String, String> request) {
        String assistantName = request.get("name");
        boolean verbose = Boolean.parseBoolean(request.getOrDefault("verbose", "false"));

        String assistantId = assistantService.createAssistant(assistantName, verbose);
        return ResponseEntity.ok(assistantId);
    }

    // user message
    @PostMapping("/{threadId}/userMessage")
    public ResponseEntity<String> createUserMessage(@RequestBody Map<String, String> request) {
        String message = request.get("userMessage");
        String response = assistantService.createUserMessage(message);
        return ResponseEntity.ok(response);
    }

    // assistant reply message
    @PostMapping("/{threadId}/assistantReply")
    public ResponseEntity<String> assistantReply(@RequestBody Map<String, String> request) {

        String message = request.get("userMessage");
        String response = assistantService.assistantReply(assistantService.getThreadId(), assistantService.getAssistantId());
        return ResponseEntity.ok(response);
    }
}