package edu.java.clients;

import edu.java.data.StackOverflowDataList;
import edu.java.data.Update;
import edu.java.entity.Link;

public interface StackOverflowClient {
    StackOverflowDataList checkQuestions(Long id);

    Update checkForUpdate(Link link);
}
