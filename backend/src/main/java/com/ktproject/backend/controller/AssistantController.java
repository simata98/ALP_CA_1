package com.ktproject.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ktproject.backend.service.Assistant;


@RestController
public class AssistantController {
    @Autowired
    Assistant assistant;

    AssistantController(Assistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/assistant")
    public String assistant(@RequestParam(defaultValue="Hello") String message) {
        return assistant.chat(message);
    }
}
