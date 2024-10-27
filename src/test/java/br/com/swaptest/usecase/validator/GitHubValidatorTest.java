package br.com.swaptest.usecase.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubValidatorTest {

    @Test
    void testValidUsername() {
        assertTrue(GitHubValidator.isValidUsername("validUser123"));
        assertTrue(GitHubValidator.isValidUsername("valid_user-name"));
        assertTrue(GitHubValidator.isValidUsername("user-name"));
        assertTrue(GitHubValidator.isValidUsername("user123"));
        assertFalse(GitHubValidator.isValidUsername("-invalid"));
        assertFalse(GitHubValidator.isValidUsername("invalid-"));
        assertFalse(GitHubValidator.isValidUsername("invalid_username!"));
        assertFalse(GitHubValidator.isValidUsername(null));
        assertFalse(GitHubValidator.isValidUsername("a".repeat(40))); // Testando limite máximo
    }

    @Test
    void testValidRepositoryName() {
        assertTrue(GitHubValidator.isValidRepositoryName("valid-repo"));
        assertTrue(GitHubValidator.isValidRepositoryName("valid_repo-name123"));
        assertTrue(GitHubValidator.isValidRepositoryName("repo-name"));
        assertTrue(GitHubValidator.isValidRepositoryName("repo123"));
        assertFalse(GitHubValidator.isValidRepositoryName("invalid repo"));
        assertFalse(GitHubValidator.isValidRepositoryName("invalid/repo"));
        assertFalse(GitHubValidator.isValidRepositoryName(null));
        assertFalse(GitHubValidator.isValidRepositoryName("a".repeat(101))); // Testando limite máximo
    }
}