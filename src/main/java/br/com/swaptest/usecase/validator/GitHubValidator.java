package br.com.swaptest.usecase.validator;

public class GitHubValidator {

    // Regex para validar nome de usuário
    private static final String USERNAME_REGEX = "^(?!-)[a-zA-Z0-9-_]{1,39}(?<!-)$";

    // Regex para validar nome de repositório
    private static final String REPO_NAME_REGEX = "^[a-zA-Z0-9-_]{1,100}$";

    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        return username.matches(USERNAME_REGEX);
    }

    public static boolean isValidRepositoryName(String repoName) {
        if (repoName == null) return false;
        return repoName.matches(REPO_NAME_REGEX);
    }


}
