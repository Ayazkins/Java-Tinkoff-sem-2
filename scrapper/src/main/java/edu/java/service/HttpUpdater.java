package edu.java.service;

import edu.java.clients.BotClient;
import edu.java.requests.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpUpdater implements MessageUpdater {
    private final BotClient botClient;

    @Override
    public void send(LinkUpdateRequest linkUpdateRequest) {
        botClient.update(linkUpdateRequest);
    }
}
