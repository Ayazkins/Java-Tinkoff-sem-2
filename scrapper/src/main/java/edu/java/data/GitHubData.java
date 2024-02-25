package edu.java.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubData(
    @JsonProperty("updated_at")
    OffsetDateTime time,

    @JsonProperty("name")
    String name
){
}
