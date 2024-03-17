package edu.java.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Payload(
    @JsonProperty("issue") IssueComment issueComment,
    @JsonProperty("pull_request") PullRequest pullRequest,

    @JsonProperty("commits") List<Push> push,

    Create create) {
    public record IssueComment(@JsonProperty("title") String title) {
    }

    public record Push(@JsonProperty("message") String message) {
    }

    public record Create(@JsonProperty("ref") String branchName) {
    }

    public record PullRequest(@JsonProperty("body") String body) {
    }
}
