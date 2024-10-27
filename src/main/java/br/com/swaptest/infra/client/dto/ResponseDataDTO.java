package br.com.swaptest.infra.client.dto;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.domain.Issue;
import java.util.List;

public record ResponseDataDTO(String user, String repository, List<Issue> issues, List<Contributor> contributors) {
}
