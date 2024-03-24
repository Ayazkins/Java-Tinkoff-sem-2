package edu.java.repository.jooq;

import edu.java.entity.jdbc.Link;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.LINK;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    public Link save(Link link) {
        dslContext.insertInto(LINK)
            .set(LINK.URL, link.getUrl())
            .execute();
        return findByUrl(link.getUrl());
    }

    @Override
    public Link delete(String url) {
        Link link = findByUrl(url);
        dslContext.delete(LINK)
            .where(LINK.URL.eq(url))
            .execute();
        return link;
    }

    @Override
    public Link findByUrl(String url) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.URL.eq(url))
            .fetchOneInto(Link.class);
    }

    @Override
    public List<Link> findAllLinksCheckedLongAgo(OffsetDateTime time) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.LAST_CHECKED.lessThan(time))
            .fetchInto(Link.class);
    }

    @Override
    public void update(String url, OffsetDateTime checked, OffsetDateTime updated) {
        dslContext.update(LINK)
            .set(LINK.LAST_UPDATED, updated)
            .set(LINK.LAST_CHECKED, checked)
            .where(LINK.URL.eq(url))
            .execute();
    }
}
