package edu.java.repository;

import edu.java.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {
    Link save(Link link);

    Link delete(String url);


    Link findByUrl(String url);

    List<Link> findAllLinksCheckedLongAgo(OffsetDateTime time);

    void update(String url, OffsetDateTime checked, OffsetDateTime updated);
}
