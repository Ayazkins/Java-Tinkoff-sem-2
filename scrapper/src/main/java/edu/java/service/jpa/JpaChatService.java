package edu.java.service.jpa;

import edu.java.entity.hibernate.Chat;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository repository;

    @Override
    @Transactional
    public void register(Long chatId) {
        if (repository.existsById(chatId)) {
            throw new IllegalStateException("chat is already registered");
        }
        repository.save(Chat
            .builder()
            .id(chatId)
            .build());
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        if (!repository.existsById(chatId)) {
            throw new IllegalStateException("chat not found");
        }
        repository.delete(repository.findById(chatId).get());
    }
}
