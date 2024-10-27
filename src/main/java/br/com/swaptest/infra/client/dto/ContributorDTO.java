package br.com.swaptest.infra.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContributorDTO(String name, String user, @JsonProperty("qtd_commits") int qtdCommits) {
}
