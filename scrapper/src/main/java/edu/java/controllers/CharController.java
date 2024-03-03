package edu.java.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tg-chat")
public class CharController {
    @PostMapping("/{id}")
    public ResponseEntity<String> createChat(@PathVariable("id") Long chatId) {
        return ResponseEntity.ok("chat is created");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable("id") Long chatId) {
        return ResponseEntity.ok("chat is deleted");
    }
}
