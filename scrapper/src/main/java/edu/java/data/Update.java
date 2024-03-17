package edu.java.data;

import java.time.OffsetDateTime;

public record Update(
    OffsetDateTime updatedAt,
    String message
) {
}
