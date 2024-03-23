package edu.java.service.jooq;

import edu.java.entity.Chat;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JooqChatService implements ChatService {
    private final JooqChatRepository chatRepository;

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
