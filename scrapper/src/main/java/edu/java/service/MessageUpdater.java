package edu.java.service;

import edu.java.requests.LinkUpdateRequest;

public interface MessageUpdater {
    void send(LinkUpdateRequest linkUpdateRequest);
}
