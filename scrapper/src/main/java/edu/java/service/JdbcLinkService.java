package edu.java.service;

import edu.java.entity.jdbc.Link;
import edu.java.repository.impl.ChatLinkRepositoryImpl;
import edu.java.repository.impl.LinkRepositoryImpl;
import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepositoryImpl linkRepository;

     private final ChatLinkRepositoryImpl chatLinkRepository;

    @Override
    @Transactional
    public LinkResponse add(Long chatId, AddLinkRequest addLinkRequest) {
        Link link = linkRepository.findByUrl(addLinkRequest.link());
        if (link != null) {
            throw new IllegalArgumentException("Link is already tracked");
        }
        Link saved = linkRepository.save(new Link(null, addLinkRequest.link(), null, null));
        chatLinkRepository.addLinkToChat(chatId, saved.getId());
        return new LinkResponse(saved.getId(), saved.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse remove(Long chatId, RemoveLinkRequest request) {
        Link link = linkRepository.findByUrl(request.link());
        if (link == null) {
            throw new IllegalArgumentException("No such link");
        }
        chatLinkRepository.removeLinkFromChat(chatId, link.getId());
        if (!chatLinkRepository.isLinkTracked(link.getId())) {
            linkRepository.delete(link.getUrl());
        }
        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    @Transactional
    public ListLinksResponse findAll(Long chatId) {
        List<LinkResponse> links = chatLinkRepository.findAllLinksByChat(chatId)
            .stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();
        return new ListLinksResponse(links, links.size());
    }
}
