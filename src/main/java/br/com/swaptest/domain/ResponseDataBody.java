package br.com.swaptest.domain;

import java.time.LocalDate;
import java.util.List;

public record ResponseDataBody(String user, String repository, LocalDate date, List<Issue> issues, List<Contributor> contributors) {
}
