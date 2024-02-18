package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.messageService.MessageProcessor;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkTrackerBot implements Bot {

    private final MessageProcessor messageProcessor;

    private final TelegramBot telegramBot;

    @Autowired
    public LinkTrackerBot(TelegramBot telegramBot, MessageProcessor messageProcessor) {
        this.telegramBot = telegramBot;
        this.messageProcessor = messageProcessor;
    }

    public SetMyCommands menu() {
        List<Command> commandList = messageProcessor.commands();
        BotCommand[] commands = new BotCommand[commandList.size()];
        for (int i = 0; i < commandList.size(); i++) {
            commands[i] = commandList.get(i).toApiCommand();
        }
        return new SetMyCommands(commands);
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null) {
                execute(messageProcessor.process(update));
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        telegramBot.setUpdatesListener(this);
        this.execute(menu());
    }

    @Override
    public void close() {
        telegramBot.removeGetUpdatesListener();
    }
}
