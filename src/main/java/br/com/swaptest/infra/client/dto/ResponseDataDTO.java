package br.com.swaptest.infra.client.dto;

import java.util.List;

public record ResponseDataDTO(String user, String repository, List<IssueDTO> issues,
                              List<ContributorDTO> contributors) {
}
