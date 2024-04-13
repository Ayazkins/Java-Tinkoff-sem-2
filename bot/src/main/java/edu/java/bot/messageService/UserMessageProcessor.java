package edu.java.bot.messageService;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandRepo;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserMessageProcessor implements MessageProcessor {

    private final static String NO_SUCH_COMMAND = "No such command. Try /help.";
    private final CommandRepo commands;

    private final MeterRegistry meterRegistry;

    @Autowired
    public UserMessageProcessor(CommandRepo commands, MeterRegistry meterRegistry) {
        this.commands = commands;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public List<Command> commands() {
        return commands.getCommands();
    }

    @Override
    public SendMessage process(Update update) {
        Optional<Command> command = commands.getCommands().stream().filter(temp -> temp.supports(update)).findAny();
        if (command.isPresent()) {
            meterRegistry.counter("commands", "command_type", command.get().command()).increment();
            return command.get().handle(update);
        }
        return new SendMessage(update.message().chat().id(), NO_SUCH_COMMAND);
    }
}
