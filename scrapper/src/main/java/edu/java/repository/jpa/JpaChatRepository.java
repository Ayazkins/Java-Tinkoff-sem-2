package edu.java.repository.jpa;

import edu.java.entity.hibernate.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
