package edu.java.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowData(
    @JsonProperty("question_id")
    Long id,
    @JsonProperty("last_activity_date")
    OffsetDateTime date
) {
}
