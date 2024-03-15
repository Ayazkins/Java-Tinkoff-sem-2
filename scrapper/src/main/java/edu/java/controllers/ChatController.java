package edu.java.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class ChatController {
    @PostMapping("/{id}")
    public ResponseEntity<String> createChat(@PathVariable("id") Long chatId) {
        return ResponseEntity.ok("chat is created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable("id") Long chatId) {
        return ResponseEntity.ok("chat is deleted");
    }
}
