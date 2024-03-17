package edu.java.repository.jooq;

import edu.java.entity.Link;
import edu.java.repository.ChatLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static edu.java.domain.jooq.Tables.LINK;

@Repository
@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DSLContext dslContext;

    @Override
    public void addLinkToChat(Long chatId, Long linkId) {
        dslContext.insertInto(CHAT_LINK)
            .set(CHAT_LINK.CHAT_ID, chatId)
            .set(CHAT_LINK.LINK_ID, linkId)
            .execute();
    }

    @Override
    public void removeLinkFromChat(Long chatId, Long linkId) {
        dslContext.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId)))
            .execute();
    }

    @Override
    public List<Link> findAllLinksByChat(Long chatId) {
        return dslContext.select(LINK)
            .from(LINK).join(CHAT_LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            .where(CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }

    @Override
    public boolean isLinkTracked(Long linkId) {
        return dslContext.fetchExists(dslContext.select()
            .from(CHAT_LINK).where(CHAT_LINK.LINK_ID.eq(linkId)));
    }

    @Override
    public List<Long> findAllChatByLink(Long linkId) {
        return dslContext.select(CHAT_LINK.CHAT_ID)
            .from(CHAT_LINK).where(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(Long.class);
    }
}
