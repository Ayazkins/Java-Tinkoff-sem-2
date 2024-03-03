package edu.java.clients;

import edu.java.requests.LinkUpdateRequest;

public interface BotClient {
    String update(LinkUpdateRequest linkUpdateRequest);
}
