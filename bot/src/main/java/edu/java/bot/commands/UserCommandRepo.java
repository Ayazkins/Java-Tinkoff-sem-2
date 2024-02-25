package edu.java.bot.commands;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCommandRepo implements CommandRepo {

    private final List<Command> commands;

    @Autowired
    public UserCommandRepo(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }
}
