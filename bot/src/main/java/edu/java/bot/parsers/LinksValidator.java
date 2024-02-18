package edu.java.bot.parsers;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class LinksValidator implements UrlValidator {

    private final List<Parser> parsers;

    @Autowired
    public LinksValidator(List<Parser> parsers) {
        this.parsers = parsers;
    }

    @Override
    public boolean isValid(URL link) {
        Optional<Parser> parser = parsers.stream().filter(a -> a.isValid(link)).findAny();
        return parser.isPresent();
    }
}
