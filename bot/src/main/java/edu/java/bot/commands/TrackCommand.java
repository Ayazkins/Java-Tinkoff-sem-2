package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.parsers.LinksValidator;
import edu.java.bot.repository.Repository;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private final static String COMMAND = "/track";

    private final static String DESCRIPTION = "Start tracking tour link";

    private final static String WRONG_LINK = "Wrong link, try another one.";

    private final static String SPACE = " - ";

    private final Repository repository;

    private final LinksValidator linksValidator;

    @Autowired
    public TrackCommand(LinksValidator linksValidator, Repository repository) {
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
            try {
                URL url = new URI(parts[1]).toURL();
                if (linksValidator.isValid(url)) {
                    String ans = DESCRIPTION + " " + parts[1] + "\n";
                    return new SendMessage(update.message().chat().id(), ans);
                }
            } catch (URISyntaxException | MalformedURLException e) {
                return new SendMessage(update.message().chat().id(), WRONG_LINK);
            }
        }
        return new SendMessage(update.message().chat().id(), WRONG_LINK);
    }
}
