package edu.java.repository.jpa;

import edu.java.entity.hibernate.Chat;
import edu.java.entity.hibernate.ChatLink;
import edu.java.entity.hibernate.Link;
import edu.java.scrapper.IntegrationTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaChatLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JpaChatLinkRepository chatLinkRepository;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Autowired
    private JpaChatRepository chatRepository;

    private Link link = Link.builder().id(1L).url("test.com").build();
    private Chat chat = Chat.builder().id(1L).build();

    @Test
    @Rollback
    @Transactional
    void addLinkToChat() {
        Link link1 = linkRepository.save(link);
        Chat chat1 = chatRepository.save(chat);


        ChatLink chatLink = new ChatLink();
        chatLink.setChat(chat1);
        chatLink.setLink(link1);

        chatLinkRepository.save(chatLink);

        assertTrue(chatLinkRepository.existsByLinkId(link.getId()));
    }

    @Test
    @Rollback
    @Transactional
    void removeLinkFromChat() {
        linkRepository.save(link);
        chatRepository.save(chat);
        link = linkRepository.findByUrl("test.com").get();
        ChatLink chatLink = new ChatLink();
        chatLink.setChat(chat);
        chatLink.setLink(link);
        chatLinkRepository.save(chatLink);

        chatLinkRepository.delete(chatLink);

        assertFalse(chatLinkRepository.existsByLinkId(link.getId()));
    }

    @Test
    @Rollback
    @Transactional
    void findAllLinksByChat() {
        Link link1 = linkRepository.save(link);
        Chat chat1 = chatRepository.save(chat);
        ChatLink chatLink = new ChatLink();
        chatLink.setChat(chat1);
        chatLink.setLink(link1);
        chatLinkRepository.save(chatLink);

        var a = chatLinkRepository.findAllLinksByChatId(chat1.getId());

        assertEquals(a.getFirst().getId(), link1.getId());

    }

    @Test
    @Rollback
    @Transactional
    void findAllChatByLink() {
        Link link1 = linkRepository.save(link);
        Chat chat1 = chatRepository.save(chat);
        ChatLink chatLink = new ChatLink();
        chatLink.setChat(chat1);
        chatLink.setLink(link1);
        chatLinkRepository.save(chatLink);

        var a = chatLinkRepository.findAllChatIdsByLinkId(link1.getId());

        assertEquals(a.getFirst(), chat.getId());

    }
}
