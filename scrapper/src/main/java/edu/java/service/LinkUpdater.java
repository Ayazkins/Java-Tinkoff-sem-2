package edu.java.service;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.data.Update;
import edu.java.entity.jdbc.Link;
import edu.java.repository.impl.ChatLinkRepositoryImpl;
import edu.java.repository.impl.LinkRepositoryImpl;
import edu.java.requests.LinkUpdateRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LinkUpdater implements Updater {

    private final static Long TIME = 10L;

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final ChatLinkRepositoryImpl chatLinkRepository;
    private final BotClient botClient;
    private final LinkRepositoryImpl linkRepository;

    @Override
    @Transactional
    public int update() {
        int count = 0;
        OffsetDateTime checkTime = OffsetDateTime.now().minusMinutes(TIME);
        List<Link> links = linkRepository.findAllLinksCheckedLongAgo(checkTime);
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
