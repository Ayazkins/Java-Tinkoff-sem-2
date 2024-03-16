package edu.java.repository;

import edu.java.entity.Link;
import java.util.List;

public interface ChatLinkRepository {
    void addLinkToChat(Long chatId, Long linkId);

    void removeLinkFromChat(Long chatId, Long linkId);

    List<Link> findAllLinksByChat(Long chatId);

    boolean isLinkTracked(Long linkId);

    List<Long> findAllChatByLink(Long linkId);

}
