package edu.java.service.jpa;

import edu.java.entity.hibernate.ChatLink;
import edu.java.entity.hibernate.Link;
import edu.java.repository.jpa.JpaChatLinkRepository;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final JpaChatLinkRepository chatLinkRepository;

    @Override
    @Transactional
    public LinkResponse add(Long chatId, AddLinkRequest addLinkRequest) {
        Optional<Link> link = linkRepository.findByUrl(addLinkRequest.link());
        if (link.isPresent() && chatLinkRepository.existsByChatIdAndLinkId(chatId, link.get().getId())) {
            throw new IllegalStateException("link is already tracked");
        }
        Link present = link.orElseGet(
            () -> linkRepository.save(
            Link.builder()
                .url(addLinkRequest.link())
                .lastChecked(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .chatLinks(new ArrayList<>())
                .build()
        )
        );

        ChatLink chatLink = new ChatLink();
        chatLink.setLink(present);
        chatLink.setChat(chatRepository.getReferenceById(chatId));
        chatLinkRepository.save(chatLink);

        return new LinkResponse(present.getId(), present.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse remove(Long chatId, RemoveLinkRequest request) {
        Optional<ChatLink> chatLink = chatLinkRepository.findChatLinkByChatIdAndLinkUrl(chatId, request.link());
        if (chatLink.isEmpty()) {
            throw new NoSuchElementException("No such tracked link");
        }
        chatLinkRepository.delete(chatLink.get());
        Link link = chatLink.get().getLink();
        if (!chatLinkRepository.existsByLinkId(link.getId())) {
            linkRepository.delete(link);
        }
        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public ListLinksResponse findAll(Long chatId) {
        List<LinkResponse> links = chatLinkRepository.findAllLinksByChatId(chatId)
            .stream().map(link -> new LinkResponse(link.getId(), link.getUrl())).toList();
        return new ListLinksResponse(links, links.size());
    }
}
