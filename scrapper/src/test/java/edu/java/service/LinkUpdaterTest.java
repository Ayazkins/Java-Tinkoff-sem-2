package edu.java.service;

import edu.java.clients.BotClient;
import edu.java.clients.GitHubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.data.Update;
import edu.java.entity.jdbc.Link;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.impl.ChatLinkRepositoryImpl;
import edu.java.repository.impl.LinkRepositoryImpl;
import edu.java.requests.LinkUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class LinkUpdaterTest {

    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private StackOverflowClient stackOverflowClient;

    @Mock
    private ChatLinkRepositoryImpl chatLinkRepository;

    @Mock
    private BotClient botClient;

    @Mock
    private LinkRepositoryImpl linkRepository;

    private Updater linkUpdater;

    @BeforeEach
    void setUp() {
        linkUpdater = new LinkUpdater(gitHubClient, stackOverflowClient, chatLinkRepository, botClient, linkRepository);
    }

    @Test
    void testUpdate() {
        List<Link> links = new ArrayList<>();
        Link link1 = new Link(1L, "https://github.com/example/example", OffsetDateTime.now(), OffsetDateTime.now().minusDays(1));
        Link link2 = new Link(2L, "https://github.com/example2/example2", OffsetDateTime.now().minusDays(10), OffsetDateTime.now().minusDays(10));
        links.add(link1);
        links.add(link2);

        Mockito.when(linkRepository.findAllLinksCheckedLongAgo(Mockito.any(OffsetDateTime.class))).thenReturn(links);
        Mockito.when(gitHubClient.checkForUpdate(Mockito.any(Link.class))).thenReturn(new Update(OffsetDateTime.now().minusDays(5), "test"));

        int count = linkUpdater.update();

        assertEquals(count, 1);
        Mockito.verify(gitHubClient, Mockito.times(2)).checkForUpdate(Mockito.any(Link.class));
        Mockito.verify(botClient, Mockito.times(1)).update(Mockito.any(LinkUpdateRequest.class));
        Mockito.verify(linkRepository, Mockito.times(2)).update(Mockito.anyString(), Mockito.any(OffsetDateTime.class), Mockito.any(OffsetDateTime.class));
    }
}
