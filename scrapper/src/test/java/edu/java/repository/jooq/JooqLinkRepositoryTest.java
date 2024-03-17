package edu.java.repository.jooq;

import edu.java.entity.Link;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JooqLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqLinkRepository linkRepository;
    @Test
    @Transactional
    @Rollback
    public void addLinkTest() {
        Link link = new Link(1L, "test.com", null, null);
        linkRepository.save(link);
        Link link1 = linkRepository.findByUrl("test.com");
        assertEquals(link1.getUrl(), "test.com");
        assertTrue(OffsetDateTime.now().toLocalDate().isEqual(link1.getLastChecked().toLocalDate()));
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        Link link = new Link(1L, "test.com", null, null);
        linkRepository.save(link);
        linkRepository.delete("test.com");
        assertNull(linkRepository.findByUrl("test.com"));
    }

    @Test
    @Transactional
    @Rollback
    public void findByUrlTest() {
        Link link = new Link(1L, "test.com", null, null);
        linkRepository.save(link);
        assertEquals("test.com", linkRepository.findByUrl("test.com").getUrl());
    }

    @Test
    @Transactional
    @Rollback
    public void findAllLinksCheckedLongTimeAgoTest() {
        Link link = new Link(1L, "test.com", null, null);
        Link link2 = new Link(2L, "test2.com", null, null);
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
        Link link = new Link(1L, "test.com", null, null);
        linkRepository.save(link);
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        linkRepository.update("test.com", offsetDateTime, offsetDateTime);
        Link link1 = linkRepository.findByUrl("test.com");
        assertEquals(link1.getUrl(), "test.com");
        assertEquals(link1.getLastChecked().toLocalDate(), offsetDateTime.toLocalDate());
    }

}
