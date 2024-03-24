package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.parsers.LinksValidator;
import edu.java.bot.requests.AddLinkRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final static String COMMAND = "/track";

    private final static String DESCRIPTION = "Start tracking your link";

    private final static String WRONG_LINK = "Wrong link, try another one.";

    private final static String SPACE = " - ";

    private final ScrapperClient scrapperClient;

    private final LinksValidator linksValidator;

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
                    scrapperClient.addLink(update.message().chat().id(), new AddLinkRequest(url.toString()));
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
