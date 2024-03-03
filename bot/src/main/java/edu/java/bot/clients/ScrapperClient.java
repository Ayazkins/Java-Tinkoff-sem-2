package edu.java.bot.clients;

import edu.java.bot.requests.AddLinkRequest;
import edu.java.bot.requests.RemoveLinkRequest;
import edu.java.bot.responses.LinkResponse;
import edu.java.bot.responses.ListLinksResponse;

public interface ScrapperClient {
    String registerChat(Long id);
    String deleteChat(Long id);

    ListLinksResponse getLinks(Long id);

    LinkResponse addLink(Long id, AddLinkRequest addLinkRequest);

    LinkResponse removeLink(Long id, RemoveLinkRequest removeLinkRequest);
}
