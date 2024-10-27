package br.com.swaptest.infra.client.converter;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.infra.client.dto.ContributorDTO;

import java.util.List;
import java.util.Optional;

public class ContributorConverter {

    public static ContributorDTO toDTO(Contributor domain) {
        return new ContributorDTO(domain.login(), domain.login(), domain.contributions());
    }

    public static List<ContributorDTO> toListDTO(List<Contributor> domainList) {
        return Optional.ofNullable(domainList).orElse(List.of()).stream().map(item -> toDTO(item)).toList();
    }
}
