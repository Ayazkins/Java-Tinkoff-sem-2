package edu.java.bot.controllers;

import edu.java.bot.requests.AddLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    @PostMapping
    public ResponseEntity<String> update(@RequestBody AddLinkRequest request) {
        return ResponseEntity.ok("test");
    }

}
