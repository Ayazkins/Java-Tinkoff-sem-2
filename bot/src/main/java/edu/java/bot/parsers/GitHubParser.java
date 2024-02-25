package edu.java.bot.parsers;

import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class GitHubParser implements Parser {

    private final static String PATTERN = "github.com";

    @Override
    public boolean isValid(URL link) {
        return link.getHost().equals(PATTERN);
    }
}
