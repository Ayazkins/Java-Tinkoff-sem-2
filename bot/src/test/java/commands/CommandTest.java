package commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.clients.ScrapperClientImpl;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandRepo;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.parsers.GitHubParser;
import edu.java.bot.parsers.LinksValidator;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.requests.AddLinkRequest;
import edu.java.bot.responses.LinkResponse;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandTest {
    private final static Update UPDATE = mock(Update.class);
    private final static CommandRepo COMMAND_REPO = mock(CommandRepo.class);

    private final static Command COMMAND = mock(Command.class);

    private final static Message MESSAGE = mock(Message.class);

    private final static Chat CHAT = mock(Chat.class);

    private final static ScrapperClient SCRAPPER_CLIENT = mock(ScrapperClientImpl.class);
    private static final String EXPECTED = "command for testing";
    private static final String COMMAND_FOR_TESTING = "/test";
    private static final String RIGHT_TRACK_COMMAND =
        "/track https://github.com/is-itmo-c-22/labwork-7-Ayazkins/tree/main/bin";
    private static final String EXPECTED_FOR_TRACK_COMMAND =
        "Start tracking your link https://github.com/is-itmo-c-22/labwork-7-Ayazkins/tree/main/bin\n";
    private static final String WRONG_COMMAND = "/track https://hello.com";
    private static final String ANSWER_FOR_WRONG_COMMAND = "Wrong link, try another one.";

    @Test
    public void helpCommandTest() {
        when(UPDATE.message()).thenReturn(MESSAGE);
        when(MESSAGE.chat()).thenReturn(CHAT);
        when(CHAT.id()).thenReturn(1L);
        when(COMMAND_REPO.getCommands()).thenReturn(List.of(COMMAND));
        when(COMMAND.command()).thenReturn(COMMAND_FOR_TESTING);
        when(COMMAND.description()).thenReturn(EXPECTED);

        Command command = new HelpCommand(COMMAND_REPO);
        SendMessage sendMessage = command.handle(UPDATE);
        assertEquals(sendMessage.getParameters().get("chat_id"), 1L);
        assertEquals(sendMessage.getParameters().get("text"), COMMAND_FOR_TESTING + " - " + EXPECTED);

    }

    @Test
    public void trackCommandTest() {
        when(UPDATE.message()).thenReturn(MESSAGE);
        when(MESSAGE.text()).thenReturn(RIGHT_TRACK_COMMAND);
        when(MESSAGE.chat()).thenReturn(CHAT);
        when(SCRAPPER_CLIENT.addLink(anyLong(), new AddLinkRequest(anyString()))).thenReturn(new LinkResponse(1L, RIGHT_TRACK_COMMAND));
        when(CHAT.id()).thenReturn(1L);

        Command command = new TrackCommand(SCRAPPER_CLIENT, new LinksValidator(List.of(new GitHubParser())));
        SendMessage sendMessage = command.handle(UPDATE);
        assertEquals(sendMessage.getParameters().get("text"),
            EXPECTED_FOR_TRACK_COMMAND
        );
        assertEquals(sendMessage.getParameters().get("chat_id"), 1L);

    }

    @Test
    public void wrongUrlTrackTest() {
        when(UPDATE.message()).thenReturn(MESSAGE);
        when(MESSAGE.text()).thenReturn(WRONG_COMMAND);
        when(MESSAGE.chat()).thenReturn(CHAT);
        when(SCRAPPER_CLIENT.addLink(anyLong(), new AddLinkRequest(anyString()))).thenReturn(new LinkResponse(1L, RIGHT_TRACK_COMMAND));
        when(CHAT.id()).thenReturn(1L);

        Command command = new TrackCommand(SCRAPPER_CLIENT, new LinksValidator(List.of(new GitHubParser())));
        SendMessage sendMessage = command.handle(UPDATE);
        assertEquals(sendMessage.getParameters().get("text"), ANSWER_FOR_WRONG_COMMAND);
        assertEquals(sendMessage.getParameters().get("chat_id"), 1L);
    }
}
