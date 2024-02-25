package edu.java.bot.repository;

import org.springframework.stereotype.Component;

@Component
public class UserRepository implements Repository {
    @Override
    public boolean isEmpty(long id) {
        return true;
    }
}
