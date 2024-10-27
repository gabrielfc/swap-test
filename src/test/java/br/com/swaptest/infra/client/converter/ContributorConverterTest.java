package br.com.swaptest.infra.client.converter;

import br.com.swaptest.domain.Contributor;
import br.com.swaptest.infra.client.dto.ContributorDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContributorConverterTest {

    @Test
    void testToDTO() {
        Contributor contributor = new Contributor("john_doe", 10);
        ContributorDTO dto = ContributorConverter.toDTO(contributor);

        assertNotNull(dto);
        assertEquals("john_doe", dto.user());
        assertEquals("john_doe", dto.name());
        assertEquals(10, dto.qtdCommits());
    }

    @Test
    void testToListDTO() {
        Contributor contributor1 = new Contributor("john_doe", 10);
        Contributor contributor2 = new Contributor("jane_doe", 5);
        List<Contributor> contributors = Arrays.asList(contributor1, contributor2);

        List<ContributorDTO> dtos = ContributorConverter.toListDTO(contributors);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        assertEquals("john_doe", dtos.get(0).user());
        assertEquals(10, dtos.get(0).qtdCommits());

        assertEquals("jane_doe", dtos.get(1).user());
        assertEquals(5, dtos.get(1).qtdCommits());
    }

    @Test
    void testToListDTO_EmptyList() {
        List<ContributorDTO> dtos = ContributorConverter.toListDTO(List.of());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void testToListDTO_NullList() {
        List<ContributorDTO> dtos = ContributorConverter.toListDTO(null);

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}
