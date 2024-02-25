package edu.java.bot.messageService;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;

public interface MessageProcessor {
    List<Command> commands();

    SendMessage process(Update update);
}