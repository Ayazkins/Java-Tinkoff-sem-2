package messageService;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandRepo;
import edu.java.bot.messageService.MessageProcessor;
import edu.java.bot.messageService.UserMessageProcessor;
import org.junit.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserMessageProcessorTest {
    private final static CommandRepo COMMAND_REPO = mock(CommandRepo.class);
    private final static Update UPDATE = mock(Update.class);

    private final static Message MESSAGE = mock(Message.class);
    private final static Chat CHAT = mock(Chat.class);

    private final Command COMMAND = mock(Command.class);

    @Test
    public void messageProcessorTest() {
        when(UPDATE.message()).thenReturn(MESSAGE);
        when(MESSAGE.chat()).thenReturn(CHAT);
        when(MESSAGE.text()).thenReturn("/test");
        when(CHAT.id()).thenReturn(1L);
        when(COMMAND_REPO.getCommands()).thenReturn(List.of(COMMAND));
        when(COMMAND.command()).thenReturn("/test");
        when(COMMAND.supports(UPDATE)).thenCallRealMethod();
        when(COMMAND.handle(UPDATE)).thenReturn(new SendMessage(1, "executed"));

        MessageProcessor messageProcessor = new UserMessageProcessor(COMMAND_REPO);
        SendMessage sendMessage = messageProcessor.process(UPDATE);

        assertEquals(sendMessage.getParameters().get("text"), "executed");
    }

    @Test
    public void wrongCommandMessageProcessorTest() {
        when(UPDATE.message()).thenReturn(MESSAGE);
        when(MESSAGE.chat()).thenReturn(CHAT);
        when(MESSAGE.text()).thenReturn("/test");
        when(CHAT.id()).thenReturn(1L);
        when(COMMAND_REPO.getCommands()).thenReturn(List.of(COMMAND));
        when(COMMAND.command()).thenReturn("/test2");
        when(COMMAND.handle(UPDATE)).thenReturn(new SendMessage(1, "executed"));
        when(COMMAND.supports(UPDATE)).thenCallRealMethod();

        MessageProcessor messageProcessor = new UserMessageProcessor(COMMAND_REPO);
        SendMessage sendMessage = messageProcessor.process(UPDATE);

        assertEquals(sendMessage.getParameters().get("text"), "No such command. Try /help.");
    }
}
