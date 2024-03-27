package edu.java.repository.jooq;

import edu.java.entity.jdbc.Chat;
import edu.java.entity.jdbc.Link;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JooqChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatLinkRepository chatLinkRepository;

    @Autowired
    private JooqLinkRepository linkRepository;

    @Autowired
    private JooqChatRepository chatRepository;

    Link link = new Link(1L, "test.com", null, null);
    Chat chat = new Chat(1L);

    @Test
    @Rollback
    @Transactional
    void addLinkToChat() {
        linkRepository.save(link);
        chatRepository.save(chat);
        link = linkRepository.findByUrl("test.com");

        chatLinkRepository.addLinkToChat(chat.getId(), link.getId());

        assertTrue(chatLinkRepository.isLinkTracked(link.getId()));
    }

    @Test
    @Rollback
    @Transactional
    void removeLinkFromChat() {
        linkRepository.save(link);
        chatRepository.save(chat);
        link = linkRepository.findByUrl("test.com");
        chatLinkRepository.addLinkToChat(chat.getId(), link.getId());

        chatLinkRepository.removeLinkFromChat(chat.getId(), link.getId());

        assertFalse(chatLinkRepository.isLinkTracked(link.getId()));
    }

    @Test
    @Rollback
    @Transactional
    void findAllLinksByChat() {
        linkRepository.save(link);
        chatRepository.save(chat);
        link = linkRepository.findByUrl("test.com");
        chatLinkRepository.addLinkToChat(chat.getId(), link.getId());
        Link link1 = new Link(2L, "test2.com", null, null);
        Chat chat1 = new Chat(2L);
        linkRepository.save(link1);
        chatRepository.save(chat1);
        link1 = linkRepository.findByUrl("test2.com");
        chatLinkRepository.addLinkToChat(chat1.getId(), link1.getId());

        var a = chatLinkRepository.findAllLinksByChat(1L);
        var b = chatLinkRepository.findAllLinksByChat(2L);

        assertEquals(a.getFirst().getId(), link.getId());
        assertEquals(a.getFirst().getUrl(), link.getUrl());

        assertEquals(b.getFirst().getId(), link1.getId());
        assertEquals(b.getFirst().getUrl(), link1.getUrl());

    }

    @Test
    @Rollback
    @Transactional
    void findAllChatByLink() {
        linkRepository.save(link);
        chatRepository.save(chat);
        link = linkRepository.findByUrl("test.com");
        chatLinkRepository.addLinkToChat(chat.getId(), link.getId());
        Link link1 = new Link(2L, "test2.com", null, null);
        Chat chat1 = new Chat(2L);
        linkRepository.save(link1);
        chatRepository.save(chat1);
        link1 = linkRepository.findByUrl("test2.com");
        chatLinkRepository.addLinkToChat(chat1.getId(), link1.getId());

        var a = chatLinkRepository.findAllChatByLink(link.getId());
        var b = chatLinkRepository.findAllChatByLink(link1.getId());

        assertEquals(a.getFirst(), chat.getId());
        assertEquals(b.getFirst(), chat1.getId());

    }
}
