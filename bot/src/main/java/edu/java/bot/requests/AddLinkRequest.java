package edu.java.bot.requests;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record AddLinkRequest (
    @NotBlank(message = "link should not be empty")
    String link
) {
}
