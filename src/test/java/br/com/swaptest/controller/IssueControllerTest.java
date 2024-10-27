package br.com.swaptest.controller;

import br.com.swaptest.controller.dto.UserInfoDTO;
import br.com.swaptest.usecase.GetRepositoryDataUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class IssueControllerTest {

    @Mock
    private GetRepositoryDataUseCase getRepositoryDataUseCase;

    @InjectMocks
    private IssueController issueController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessRepository_Success() {
        UserInfoDTO userInfo = new UserInfoDTO("user", "repository");

        when(getRepositoryDataUseCase.processRepositoryData(any(), any())).thenReturn(Mono.empty());

        Mono<ResponseEntity<String>> response = issueController.processRepository(userInfo);

        StepVerifier.create(response)
                .expectNext(ResponseEntity.ok(IssueController.SUCCESS_MSG))
                .verifyComplete();
    }

    @Test
    void testProcessRepository_Error() {
        UserInfoDTO userInfo = new UserInfoDTO("user", "repository");

        when(getRepositoryDataUseCase.processRepositoryData(any(), any()))
                .thenReturn(Mono.error(new RuntimeException("Erro de processamento")));

        Mono<ResponseEntity<String>> response = issueController.processRepository(userInfo);

        StepVerifier.create(response)
                .expectNext(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro de processamento"))
                .verifyComplete();
    }
}