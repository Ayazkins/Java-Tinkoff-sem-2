package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.responses.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final static String COMMAND = "/list";

    private final static String DESCRIPTION = "Current links: ";

    private final static String NO_LINKS = "No links in your list";

    private final static String SPACE = " - ";

    private final ScrapperClient scrapperClient;

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
        ListLinksResponse listLinksResponse = scrapperClient.getLinks(update.message().chat().id());

        if (listLinksResponse.links() == null || listLinksResponse.links().isEmpty()) {
            return new SendMessage(update.message().chat().id(), NO_LINKS);
        }
        StringBuilder message = new StringBuilder();
        message.append(DESCRIPTION + '\n');
        for (var link : listLinksResponse.links()) {
            message.append(link.url()).append('\n');
        }
        return new SendMessage(update.message().chat().id(), message.toString());
    }
}
