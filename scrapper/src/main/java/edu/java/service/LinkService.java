package edu.java.service;

import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;

public interface LinkService {
    LinkResponse add(Long chatId, AddLinkRequest addLinkRequest);

    LinkResponse remove(Long chatId, RemoveLinkRequest request);

    ListLinksResponse findAll(Long chatId);
}
