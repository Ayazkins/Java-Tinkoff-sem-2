package edu.java.controllers;

import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
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
    public ListLinksResponse getLinks(@RequestHeader long chatId) {
        return linkService.findAll(chatId);
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader long chatId, @RequestBody AddLinkRequest request) {
        return linkService.add(chatId, request);
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader long chatId, @RequestBody RemoveLinkRequest request) {
        return linkService.remove(chatId, request);
    }

}

