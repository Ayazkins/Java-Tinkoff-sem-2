package edu.java.repository.jooq;

import edu.java.entity.jdbc.Chat;
import edu.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dslContext;

    @Override
    public Chat save(Chat chat) {
        dslContext.insertInto(CHAT).set(CHAT.ID, chat.getId()).execute();
        return findById(chat.getId());
    }

    @Override
    public Chat delete(Long chatId) {
        Chat chat = findById(chatId);
        dslContext.delete(CHAT).where(CHAT.ID.eq(chatId)).execute();
        return chat;
    }

    @Override
    public Chat findById(Long id) {
        return dslContext.select(CHAT.fields()).from(CHAT).where(CHAT.ID.eq(id)).fetchOneInto(Chat.class
        );
    }
}
