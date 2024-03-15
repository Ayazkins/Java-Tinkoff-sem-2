package edu.java.requests;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(
    @NotBlank(message = "link shoudld not be empty")
    String link
) {
}
