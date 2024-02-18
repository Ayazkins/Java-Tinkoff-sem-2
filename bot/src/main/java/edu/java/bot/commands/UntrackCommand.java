package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.parsers.LinksValidator;
import edu.java.bot.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    private final static String COMMAND = "/untrack";

    private final static String DESCRIPTION = "Stop tracking your link";

    private final static String WRONG_LINK = "Wrong link. Try again";

    private final static String SPACE = " - ";

    private final Repository repository;

    private final LinksValidator linksValidator;

    @Autowired
    public UntrackCommand(LinksValidator linksValidator, Repository repository) {
        this.repository = repository;
        this.linksValidator = linksValidator;
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
        var parts = update.message().text().split("\\s+", 2);
        if (parts.length == 2) {
            String ans = DESCRIPTION + " " + parts[1] + "\n";
            return new SendMessage(update.message().chat().id(), ans);
        }
        return new SendMessage(update.message().chat().id(), WRONG_LINK);
    }
}
