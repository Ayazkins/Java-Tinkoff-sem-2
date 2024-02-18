package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

    private final static String COMMAND = "/help";

    private final static String DESCRIPTION = "Available commands list";

    private final static String SPACE = " - ";

    private final CommandRepo commandRepo;

    @Autowired
    public HelpCommand(@Lazy CommandRepo commandRepo) {
        this.commandRepo = commandRepo;
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
        StringBuilder stringBuilder = new StringBuilder();
        for (var a : commandRepo.getCommands()) {
            stringBuilder.append(a.command()).append(SPACE).append(a.description());
        }
        return new SendMessage(update.message().chat().id(), stringBuilder.toString());
    }
}
