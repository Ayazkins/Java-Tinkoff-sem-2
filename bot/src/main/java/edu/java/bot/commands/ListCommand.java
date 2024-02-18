package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final static String COMMAND = "/list";

    private final static String DESCRIPTION = "Current links: ";

    private final static String NO_LINKS = "No links in your list";

    private final static String SPACE = " - ";

    private final Repository repository;

    @Autowired
    public ListCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        if (repository.isEmpty(update.message().chat().id())) {
            return new SendMessage(update.message().chat().id(), NO_LINKS);
        }
        return new SendMessage(update.message().chat().id(), DESCRIPTION);
    }
}
