package edu.java.repository.jpa;

import edu.java.entity.hibernate.Chat;
import edu.java.repository.impl.ChatRepositoryImpl;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JpaChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaChatRepository chatRepository;
    @Test
    @Transactional
    @Rollback
    public void saveTest() {
        Chat chat = Chat.builder().id(1L).build();
        chatRepository.save(chat);

        Chat chat1 = chatRepository.findById(1L).get();

        assertNotNull(chat1);
        assertEquals(chat1.getId(), 1L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        Chat chat = Chat.builder().id(1L).build();

        chatRepository.save(chat);
        chatRepository.delete(chat);

        assertTrue(chatRepository.findById(1L).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void findByIdTest() {
        Chat chat = Chat.builder().id(1L).build();
        Chat chat1 = Chat.builder().id(2L).build();
        Chat chat2 = Chat.builder().id(100L).build();

        chatRepository.save(chat);
        chatRepository.save(chat1);
        chatRepository.save(chat2);
        Chat out = chatRepository.findById(100L).get();

        assertNotNull(out);
        assertEquals(out.getId(), 100L);
    }

}
