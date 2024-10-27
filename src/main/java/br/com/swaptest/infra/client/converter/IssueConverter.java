package br.com.swaptest.infra.client.converter;

import br.com.swaptest.domain.Issue;
import br.com.swaptest.infra.client.dto.IssueDTO;

import java.util.List;
import java.util.Optional;

public class IssueConverter {

    public static IssueDTO toDTO(Issue domain) {
        return new IssueDTO(domain.title(), Optional.ofNullable(domain.user()).orElse(new Issue.User("")).login(), Optional.ofNullable(domain.labels()).orElse(List.of()).stream().map(Issue.Label::name).toArray(String[]::new));
    }

    public static List<IssueDTO> toListDTO(List<Issue> domainList) {
        return Optional.ofNullable(domainList).orElse(List.of()).stream().map(item -> toDTO(item)).toList();
    }
}
