package edu.java.clients;

import edu.java.data.StackOverflowDataList;
import edu.java.entity.Link;
import java.time.OffsetDateTime;

public interface StackOverflowClient {
    StackOverflowDataList checkQuestions(Long id);

    OffsetDateTime checkForUpdate(Link link);
}
