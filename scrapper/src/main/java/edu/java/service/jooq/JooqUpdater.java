package edu.java.service.jooq;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.data.Update;
import edu.java.entity.Link;
import edu.java.repository.jooq.JooqChatLinkRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.requests.LinkUpdateRequest;
import edu.java.service.Updater;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class JooqUpdater implements Updater {
    private final static OffsetDateTime TIME = OffsetDateTime.now().minusSeconds(10L);

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final JooqChatLinkRepository chatLinkRepository;
    private final BotClient botClient;
    private final JooqLinkRepository linkRepository;

    @Override
    @Transactional
    public int update() {
        int count = 0;
        List<Link> links = linkRepository.findAllLinksCheckedLongAgo(TIME);
        for (Link link : links) {
            String host;
            try {
                URL url = new URL(link.getUrl());
                host = url.getHost();
            } catch (MalformedURLException e) {
                return 0;
            }
            Update lastUpdated;
            if (host.equals("github.com")) {
                lastUpdated = gitHubClient.checkForUpdate(link);
            } else {
                lastUpdated = stackOverflowClient.checkForUpdate(link);
            }

            if (lastUpdated.updatedAt().isAfter(link.getLastUpdated())) {
                botClient.update(new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl(),
                    lastUpdated.message(),
                    chatLinkRepository.findAllChatByLink(link.getId())
                ));
                ++count;
            }
            linkRepository.update(link.getUrl(), OffsetDateTime.now(), lastUpdated.updatedAt());
        }

        return count;
    }
}
