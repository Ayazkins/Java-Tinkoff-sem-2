package edu.java.service;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.entity.Link;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.requests.LinkUpdateRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkUpdater implements Updater {
    private final static OffsetDateTime TIME = OffsetDateTime.now().minusMinutes(10L);

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final ChatLinkRepository chatLinkRepository;
    private final BotClient botClient;
    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public int update() {
        int count = 0;
        List<Link> links = linkRepository.findAllLinksCheckedLongAgo(TIME);
        for (Link link : links) {
            OffsetDateTime lastUpdated = link.getLastUpdated();
            String host;
            try {
                URL url = new URL(link.getUrl());
                host = url.getHost();
            } catch (MalformedURLException e) {
                return 0;
            }
            if (host.equals("github.com")) {
                lastUpdated = gitHubClient.checkForUpdate(link);
            } else {
                lastUpdated = stackOverflowClient.checkForUpdate(link);
            }

            if (lastUpdated.isAfter(link.getLastUpdated())) {
                botClient.update(new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl(),
                    "update",
                    chatLinkRepository.findAllChatByLink(link.getId())
                ));
                ++count;
            }
            linkRepository.update(link.getUrl(), OffsetDateTime.now(), lastUpdated);
        }

        return count;
    }
}
