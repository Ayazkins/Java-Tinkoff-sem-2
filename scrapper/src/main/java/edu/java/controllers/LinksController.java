package edu.java.controllers;

import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public ResponseEntity<String> getLinks(@RequestHeader long chatId) {
        return ResponseEntity.ok("links");
    }

    @PostMapping
    public ResponseEntity<String> addLink(@RequestHeader long chatId, @RequestBody AddLinkRequest request) {
        return ResponseEntity.ok(request.link() + "is now added");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteLink(@RequestHeader long chatId, @RequestBody RemoveLinkRequest request) {
        return ResponseEntity.ok(request.link() + " is deleted");
    }

}

