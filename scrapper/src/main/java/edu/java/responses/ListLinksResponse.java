package edu.java.responses;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> responseList,
    int size
) {
}
