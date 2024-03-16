package edu.java.repository.impl;

import edu.java.entity.Link;
import edu.java.repository.ChatLinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class ChatLinkRepositoryImpl implements ChatLinkRepository {

    private final static String ADD = """
        insert into chat_link (chat_id, link_id) values (?, ?)
        """;

    private final static String REMOVE = """
        delete from chat_link where chat_id = ? and link_id = ?
        """;

    private final static String FIND_ALL_BY_ID = """
        select link.id, link.url, link.last_checked, link.last_updated
         from link as link join chat_link as cl on link.id = cl.link_id where cl.chat_id = ?
        """;

    private final static String IS_LINK_TRACKED = """
        select count(*) from chat_link where link_id = ?;
        """;

    private final static String FIND_ALL_CHATS_BY_LINK = """
        select chat_id from chat_link where link_id = ?
        """;

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
