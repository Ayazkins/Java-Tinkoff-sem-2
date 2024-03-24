package edu.java.repository.impl;

import edu.java.entity.jdbc.Link;
import edu.java.repository.ChatLinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import static edu.java.repository.impl.repositoryQuery.ChatLinkQuery.ADD;
import static edu.java.repository.impl.repositoryQuery.ChatLinkQuery.FIND_ALL_BY_ID;
import static edu.java.repository.impl.repositoryQuery.ChatLinkQuery.FIND_ALL_CHATS_BY_LINK;
import static edu.java.repository.impl.repositoryQuery.ChatLinkQuery.IS_LINK_TRACKED;
import static edu.java.repository.impl.repositoryQuery.ChatLinkQuery.REMOVE;

@RequiredArgsConstructor
@Repository
public class ChatLinkRepositoryImpl implements ChatLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLinkToChat(Long chatId, Long linkId) {
        jdbcTemplate.update(ADD, chatId, linkId);
    }

    @Override
    public void removeLinkFromChat(Long chatId, Long linkId) {
        jdbcTemplate.update(REMOVE, chatId, linkId);
    }

    @Override
    public List<Link> findAllLinksByChat(Long chatId) {
        return jdbcTemplate.query(FIND_ALL_BY_ID, (rs, rowNum) -> new Link(
            rs.getLong("id"),
            rs.getString("url"),
            rs.getTimestamp("last_checked")
                .toLocalDateTime()
                .atOffset(OffsetDateTime.now().getOffset()),
            rs.getTimestamp("last_updated")
                .toLocalDateTime()
                .atOffset(OffsetDateTime.now().getOffset())
        ), chatId);
    }

    public boolean isLinkTracked(Long linkId) {
        int count = jdbcTemplate.queryForObject(IS_LINK_TRACKED, Integer.class, linkId);
        return count > 0;
    }

    @Override
    public List<Long> findAllChatByLink(Long linkId) {
        return jdbcTemplate.queryForList(FIND_ALL_CHATS_BY_LINK, Long.class, linkId);
    }
}
