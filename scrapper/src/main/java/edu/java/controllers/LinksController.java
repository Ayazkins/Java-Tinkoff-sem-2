package edu.java.controllers;

import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinksController {
    private final LinkService linkService;

    @GetMapping
    public ResponseEntity<String> getLinks(@RequestHeader long chatId) {
        linkService.findAll(chatId);
        return ResponseEntity.ok("links");
    }

    @PostMapping
    public ResponseEntity<String> addLink(@RequestHeader long chatId, @RequestBody AddLinkRequest request) {
        linkService.add(chatId, request);
        return ResponseEntity.ok(request.link() + "is now added");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteLink(@RequestHeader long chatId, @RequestBody RemoveLinkRequest request) {
        linkService.remove(chatId, request);
        return ResponseEntity.ok(request.link() + " is deleted");
    }

}

