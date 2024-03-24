package edu.java.repository.jpa;

import edu.java.entity.hibernate.Link;
import edu.java.scrapper.IntegrationTest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private EntityManager entityManager;
    @Test
    @Transactional
    @Rollback
    public void addLinkTest() {
        Link link = Link.builder().url("test.com").id(1L).build();
        linkRepository.save(link);
        Link link1 = linkRepository.findByUrl("test.com").get();
        assertEquals(link1.getUrl(), "test.com");
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        Link link = Link.builder().url("test.com").id(1L).build();
        linkRepository.save(link);
        linkRepository.delete(link);
        assertTrue(linkRepository.findByUrl("test.com").isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void findByUrlTest() {
        Link link = Link.builder().url("test.com").id(1L).build();
        linkRepository.save(link);
        assertEquals("test.com", linkRepository.findByUrl("test.com").get().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void findAllLinksCheckedLongTimeAgoTest() {
        Link link = Link.builder().url("test.com").id(1L).build();
        Link link2 = Link.builder().url("test2.com").id(2L).build();

        linkRepository.save(link);
        linkRepository.save(link2);
        linkRepository.update("test.com", OffsetDateTime.now().minusDays(10), OffsetDateTime.now().minusDays(10));
        var out = linkRepository.findAllLinksCheckedLongAgo(OffsetDateTime.now().minusDays(1));
        assertEquals(out.size(), 1);
        assertEquals(out.getFirst().getUrl(), "test.com");
    }

    @Test
    @Transactional
    @Rollback
    public void updateTest() {
        Link link = Link.builder().url("test.com").id(1L).build();
        linkRepository.save(link);
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        linkRepository.update("test.com", offsetDateTime, offsetDateTime);
        entityManager.clear();
        Link link1 = linkRepository.findByUrl("test.com").get();
        assertEquals(link1.getUrl(), "test.com");
        assertEquals(link1.getLastUpdated().toLocalDate(), offsetDateTime.toLocalDate());
    }

}
