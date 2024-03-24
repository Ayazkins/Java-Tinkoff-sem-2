package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private static final String COMMAND = "/start";

    private static final String DESCRIPTION = "Bot starts";

    @Override
    public String command() {
        return COMMAND;
    }

    private final ScrapperClient scrapperClient;

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        scrapperClient.registerChat(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), DESCRIPTION);
    }
}
