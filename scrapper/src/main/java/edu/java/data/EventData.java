package edu.java.data;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventData {
    CREATE_EVENT {
        @Override
        public String message(Payload payload) {
            return "new branch: " + payload.create().branchName();
        }
    },
    PUSH_EVENT {
        @Override
        public String message(Payload payload) {
            return "new commit: " + payload.push().getFirst().message();
        }
    },
    PULL_REQUEST_EVENT {
        @Override
        public String message(Payload payload) {
            return "new pull request: " + payload.pullRequest().body();
        }
    },
    ISSUE_COMMENT_EVENT {
        @Override
        public String message(Payload payload) {
            return "new comment: " + payload.issueComment().title();
        }
    },
    NO_INFO {
        @Override
        public String message(Payload payload) {
            return "unknown change happened";
        }
    };

    public abstract String message(Payload payload);

    @JsonCreator
    public static EventData matchEvent(String value) {
        return switch (value) {
            case "CreateEvent" -> CREATE_EVENT;
            case "PushEvent" -> PUSH_EVENT;
            case "PullRequestEvent" -> PULL_REQUEST_EVENT;
            case "IssueCommentEvent" -> ISSUE_COMMENT_EVENT;
            default -> NO_INFO;
        };
    }
}
