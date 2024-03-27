package edu.java.bot.controllers;

import edu.java.bot.requests.LinkUpdateRequest;
import edu.java.bot.service.BotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdateController {

    private final BotService botService;

    @PostMapping
    public ResponseEntity<String> update(@RequestBody @Valid LinkUpdateRequest request) {
        botService.update(request);
        return ResponseEntity.ok("ok");
    }

}
