package edu.java.service.jpa;

import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.data.Update;
import edu.java.entity.hibernate.Link;
import edu.java.repository.jpa.JpaChatLinkRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.requests.LinkUpdateRequest;
import edu.java.service.MessageUpdater;
import edu.java.service.Updater;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkUpdater implements Updater {
    private final static long TIME = 10;
    private final JpaLinkRepository linkRepository;
    private final GitHubClient gitHubClient;

    private final StackOverflowClient stackOverflowClient;

    private final MessageUpdater messageUpdater;

    private final JpaChatLinkRepository chatLinkRepository;

    @Override
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
            edu.java.entity.jdbc.Link jdbcLink = new edu.java.entity.jdbc.Link(
                link.getId(),
                link.getUrl(),
                link.getLastChecked(),
                link.getLastUpdated()
            );
            if (host.equals("github.com")) {
                lastUpdated = gitHubClient.checkForUpdate(jdbcLink);
            } else {
                lastUpdated = stackOverflowClient.checkForUpdate(jdbcLink);
            }

            if (link.getLastUpdated() == null || lastUpdated.updatedAt().isAfter(link.getLastUpdated())) {
                messageUpdater.send(new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl(),
                    lastUpdated.message(),
                    chatLinkRepository.findAllChatIdsByLinkId(link.getId())
                ));
                ++count;
            }
            linkRepository.update(link.getUrl(), OffsetDateTime.now(), lastUpdated.updatedAt());
        }

        return count;
    }
}
