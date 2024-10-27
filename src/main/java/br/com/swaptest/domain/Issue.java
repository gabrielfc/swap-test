package br.com.swaptest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record Issue(
        String title,
        User user,
        List<Label> labels,
        @JsonProperty("created_at") OffsetDateTime createdAt
) {
    public record User(String login) {
    }

    public record Label(String name) {
    }
}