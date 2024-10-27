package br.com.swaptest.controller;

import br.com.swaptest.controller.dto.UserInfoDTO;
import br.com.swaptest.usecase.GetRepositoryDataUseCase;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class IssueController {
    private final GetRepositoryDataUseCase getRepositoryDataUseCase;

    public IssueController(GetRepositoryDataUseCase getRepositoryDataUseCase) {
        this.getRepositoryDataUseCase = getRepositoryDataUseCase;
    }
    @PostMapping("/process-repository")
    public Mono<Void> processRepository(@RequestBody UserInfoDTO userInfo) {
        return getRepositoryDataUseCase.processRepositoryData(userInfo.user(), userInfo.repository());
    }
}
