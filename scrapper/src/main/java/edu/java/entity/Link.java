package edu.java.entity;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime lastChecked;
    private OffsetDateTime lastUpdated;
}
