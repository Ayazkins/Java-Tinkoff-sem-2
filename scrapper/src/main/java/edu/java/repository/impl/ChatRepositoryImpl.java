package edu.java.repository.impl;

import edu.java.entity.Chat;
import edu.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepository {

    private static final String SAVE = """
        insert into chat (id) values (?)
        """;

    private static final String DELETE = """
        delete from chat where id = (?)
        """;

    private static final String FIND = """
        select id from chat where id = (?)
         """;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Chat save(Chat chat) {
        jdbcTemplate.update(SAVE, chat.getId());
        return findById(chat.getId());
    }

    @Override
    public Chat delete(Long chatId) {
        Chat chat = findById(chatId);
        jdbcTemplate.update(DELETE, chatId);
        return chat;
    }

    @Override
    public Chat findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(FIND, (rs, rowNum) -> new Chat(rs.getLong("id")), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
