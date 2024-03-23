package edu.java.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubEventsData(
    @JsonProperty("type") EventData eventData,
    @JsonProperty("payload") Payload payload,
    @JsonProperty("created_at") OffsetDateTime createdAt
) {
}
