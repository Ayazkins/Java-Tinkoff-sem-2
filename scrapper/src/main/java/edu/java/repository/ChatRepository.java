package edu.java.repository;

import edu.java.entity.Chat;

public interface ChatRepository {
    Chat save(Chat chat);

    Chat delete(Long chatId);

    Chat findById(Long id);
}
