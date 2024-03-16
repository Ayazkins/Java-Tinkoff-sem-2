package edu.java.repository.impl;

import edu.java.entity.Link;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class LinkRepositoryImpl implements LinkRepository {

    private static final String SAVE = """
        insert into link (url) values (?)
        """;

    private final static String DELETE = """
        delete from link where url = (?)
        """;

    private static final String FIND = """
        select * from link where url = ?
        """;

    private static final String FIND_DATE = """
        select id, url, last_checked, last_updated from link where last_checked < ?
        """;

    private static final String UPDATE = """
        update link set last_checked = ?, last_updated = ?
        where url = ?;
        """;
    public static final String URL = "url";
    public static final String LAST_CHECKED = "last_checked";
    public static final String LAST_UPDATED = "last_updated";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Link save(Link link) {
        jdbcTemplate.update(SAVE, link.getUrl());
        return findByUrl(link.getUrl());
    }

    @Override
    public Link delete(String url) {
        Link link = findByUrl(url);
        jdbcTemplate.update(DELETE, url);
        return link;
    }

    @Override
    public Link findByUrl(String url) {
        try {
            return jdbcTemplate.queryForObject(
                FIND,
                (rs, rowNum) -> new Link(
                    rs.getLong("id"),
                    rs.getString(URL),
                    rs.getTimestamp(LAST_CHECKED)
                        .toLocalDateTime()
                        .atOffset(OffsetDateTime.now().getOffset()),
                    rs.getTimestamp(LAST_UPDATED)
                        .toLocalDateTime()
                    .atOffset(OffsetDateTime.now().getOffset())),
                url
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Link> findAllLinksCheckedLongAgo(OffsetDateTime time) {
        return jdbcTemplate.query(
            FIND_DATE,
            (rs, rowNum) -> new Link(
                rs.getLong("id"),
                rs.getString(URL),
                rs.getTimestamp(LAST_CHECKED)
                    .toLocalDateTime()
                    .atOffset(OffsetDateTime.now().getOffset()),
                rs.getTimestamp(LAST_UPDATED)
                    .toLocalDateTime()
                    .atOffset(OffsetDateTime.now().getOffset())),
            time
        );
    }

    @Override
    public void update(String url, OffsetDateTime checked, OffsetDateTime updated) {
        jdbcTemplate.update(UPDATE, checked, updated, url);
    }
}
