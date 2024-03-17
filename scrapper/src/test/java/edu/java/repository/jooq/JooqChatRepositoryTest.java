package edu.java.repository.jooq;

import edu.java.entity.Chat;
import edu.java.repository.ChatRepository;
import edu.java.scrapper.IntegrationTest;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.Tables.CHAT;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JooqChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void saveTest() {
        Chat chat = new Chat(1L);
        chatRepository.save(chat);
        Chat chat1 = chatRepository.findById(1L);
        assertNotNull(chat1);
        assertEquals(chat1.getId(), 1L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        Chat chat = new Chat(1L);
        chatRepository.save(chat);
        chatRepository.delete(1L);
        Chat chat1 = chatRepository.findById(1L);
        assertNull(chat1);
    }

    @Test
    @Transactional
    @Rollback
    public void findByIdTest() {
        Chat chat = new Chat(1L);
        Chat chat1 = new Chat(2L);
        Chat chat2 = new Chat(100L);
        chatRepository.save(chat);
        chatRepository.save(chat1);
        chatRepository.save(chat2);
        Chat out = chatRepository.findById(100L);
        assertNotNull(out);
        assertEquals(out.getId(), 100L);
    }

}
