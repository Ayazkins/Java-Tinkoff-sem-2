package edu.java.bot.messageService;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserMessageProcessor implements MessageProcessor {

    private final static String NO_SUCH_COMMAND = "No such command. Try /help.";
    private final CommandRepo commands;

    @Autowired
    public UserMessageProcessor(CommandRepo commands) {
        this.commands = commands;
    }

    @Override
    public List<Command> commands() {
        return commands.getCommands();
    }

    @Override
    public SendMessage process(Update update) {
        Optional<Command> command = commands.getCommands().stream().filter(temp -> temp.supports(update)).findAny();
        if (command.isPresent()) {
            return command.get().handle(update);
        }
        return new SendMessage(update.message().chat().id(), NO_SUCH_COMMAND);
    }
}
