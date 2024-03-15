package edu.java.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LinkUpdateRequest(
    @NotNull(message = "chat id can not be null")
    Long chatId,
    @NotBlank(message = "url should not be blank")
    String url,

    String description,
    @NotEmpty(message = "list of tg chat id should not be empty")
    List<Long> tgChatIdList

) {
}
