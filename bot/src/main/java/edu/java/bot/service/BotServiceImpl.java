package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.Bot;
import edu.java.bot.requests.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final Bot bot;

    @Override
    public void update(LinkUpdateRequest linkUpdateRequest) {
        StringBuilder message = new StringBuilder();
        message.append("Update\n")
            .append(linkUpdateRequest.id() + '\n')
            .append(linkUpdateRequest.url()).append('\n')
            .append(linkUpdateRequest.description());
        for (Long id : linkUpdateRequest.tgChatIds()) {
            bot.execute(new SendMessage(id, message.toString()));
        }

    }
}
