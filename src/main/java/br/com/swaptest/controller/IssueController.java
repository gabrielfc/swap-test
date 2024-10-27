package br.com.swaptest.controller;

import br.com.swaptest.controller.dto.UserInfoDTO;
import br.com.swaptest.usecase.GetRepositoryDataUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class IssueController {

    public static final String SUCCESS_MSG = "Processamento concluído com sucesso!";

    private final GetRepositoryDataUseCase getRepositoryDataUseCase;

    public IssueController(GetRepositoryDataUseCase getRepositoryDataUseCase) {
        this.getRepositoryDataUseCase = getRepositoryDataUseCase;
    }

    @PostMapping("/process-repository")
    public Mono<ResponseEntity<String>> processRepository(@RequestBody UserInfoDTO userInfo) {
        return getRepositoryDataUseCase.processRepositoryData(userInfo.user(), userInfo.repository())
                .then(Mono.just(ResponseEntity.ok(SUCCESS_MSG)))
                .onErrorResume(error -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage()));
                });
    }
}
