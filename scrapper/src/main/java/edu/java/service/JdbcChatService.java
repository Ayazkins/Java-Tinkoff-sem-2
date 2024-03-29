package edu.java.service;

import edu.java.entity.jdbc.Chat;
import edu.java.repository.impl.ChatRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final ChatRepositoryImpl chatRepository;

    @Override
    @Transactional
    public void register(Long chatId) {
        Chat chat = chatRepository.findById(chatId);
        if (chat != null) {
            throw new IllegalArgumentException("Chat is already registered");
        }
        chatRepository.save(new Chat(chatId));
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        Chat chat = chatRepository.findById(chatId);
        if (chat == null) {
            throw new IllegalArgumentException("Chat not found");
        }
        chatRepository.delete(chatId);
    }
}
