package br.com.swaptest.infra.client.converter;

import br.com.swaptest.domain.Issue;
import br.com.swaptest.infra.client.dto.IssueDTO;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IssueConverterTest {

    @Test
    void testToDTO() {
        Issue.User user = new Issue.User("john_doe");
        Issue.Label label1 = new Issue.Label("bug");
        Issue.Label label2 = new Issue.Label("feature");
        List<Issue.Label> labels = Arrays.asList(label1, label2);
        Issue issue = new Issue("Sample Issue", user, labels, OffsetDateTime.now());

        IssueDTO dto = IssueConverter.toDTO(issue);

        assertNotNull(dto);
        assertEquals("Sample Issue", dto.title());
        assertEquals("john_doe", dto.author());
        assertArrayEquals(new String[]{"bug", "feature"}, dto.labels());
    }

    @Test
    void testToDTO_NullUser() {
        Issue.Label label = new Issue.Label("bug");
        List<Issue.Label> labels = List.of(label);
        Issue issue = new Issue("Sample Issue", null, labels, OffsetDateTime.now());

        IssueDTO dto = IssueConverter.toDTO(issue);

        assertNotNull(dto);
        assertEquals("Sample Issue", dto.title());
        assertEquals("", dto.author());
        assertArrayEquals(new String[]{"bug"}, dto.labels());
    }

    @Test
    void testToDTO_EmptyLabels() {
        Issue.User user = new Issue.User("john_doe");
        Issue issue = new Issue("Sample Issue", user, List.of(), OffsetDateTime.now());

        IssueDTO dto = IssueConverter.toDTO(issue);

        assertNotNull(dto);
        assertEquals("Sample Issue", dto.title());
        assertEquals("john_doe", dto.author());
        assertArrayEquals(new String[]{}, dto.labels());
    }

    @Test
    void testToListDTO() {
        Issue.User user1 = new Issue.User("john_doe");
        Issue.Label label1 = new Issue.Label("bug");
        Issue issue1 = new Issue("Issue 1", user1, List.of(label1), OffsetDateTime.now());

        Issue.User user2 = new Issue.User("jane_doe");
        Issue.Label label2 = new Issue.Label("feature");
        Issue issue2 = new Issue("Issue 2", user2, List.of(label2), OffsetDateTime.now());

        List<Issue> issues = Arrays.asList(issue1, issue2);
        List<IssueDTO> dtos = IssueConverter.toListDTO(issues);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Issue 1", dtos.get(0).title());
        assertEquals("john_doe", dtos.get(0).author());
        assertArrayEquals(new String[]{"bug"}, dtos.get(0).labels());
        assertEquals("Issue 2", dtos.get(1).title());
        assertEquals("jane_doe", dtos.get(1).author());
        assertArrayEquals(new String[]{"feature"}, dtos.get(1).labels());
    }

    @Test
    void testToListDTO_EmptyList() {
        List<IssueDTO> dtos = IssueConverter.toListDTO(List.of());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void testToListDTO_NullList() {
        List<IssueDTO> dtos = IssueConverter.toListDTO(null);

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}