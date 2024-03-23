package edu.java.service.jooq;

import edu.java.entity.Link;
import edu.java.repository.jooq.JooqChatLinkRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import edu.java.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository linkRepository;
    private final JooqChatLinkRepository chatLinkRepository;
    private final JooqChatRepository chatRepository;

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
