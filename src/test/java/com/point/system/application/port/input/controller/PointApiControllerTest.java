package com.point.system.application.port.input.controller;

import com.point.system.application.PointRequestHandler;
import com.point.system.application.dto.PointIssueCommand;
import com.point.system.application.dto.PointIssueResponse;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private PointRequestHandler pointRequestHandler;

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    private String baseUrl;
    private PointIssueCommand mockCommand;

    @BeforeAll
    void setUp() {
        // 랜덤 포트 기반의 기본 URL 설정
        baseUrl = "http://localhost:" + port + "/api/v1/point";

        // 목업 요청 객체 생성
        mockCommand = PointIssueCommand.builder()
                .requestId("test-request-123")
                .userId("user-A")
                .amount(100L)
                .build();
    }

    @DisplayName("Bulkhead 내에서 작업이 성공적으로 처리되는지 확인")
    @Test
    void shouldReturnOk() {
        PointIssueResponse expectedResponse = new PointIssueResponse("test-request-id", 1L, "test-success");
        when(pointRequestHandler.issuePoint(any(PointIssueCommand.class)))
                .thenAnswer(invocation -> {
                    Thread.sleep(100);
                    return expectedResponse;
                });

        ResponseEntity<PointIssueResponse> responseEntity = restTemplate.postForEntity(
                baseUrl,
                mockCommand,
                PointIssueResponse.class
        );

        // 검증
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @DisplayName("Bulkhead의 최대 동시 처리 요청 수를 초과했을 때 Fallback이 호출되는지 확인")
    @Test
    void shouldReturnFallbackResponse_when_Bulkheadfull() throws Exception {

        when(pointRequestHandler.issuePoint(any(PointIssueCommand.class)))
                .thenAnswer(invocation -> {
                    Thread.sleep(5000);
                    return new PointIssueResponse("test-request-id", 1L, "should-not-return");
                });

        CompletableFuture<ResponseEntity<PointIssueResponse>> future1
                = CompletableFuture.supplyAsync(() -> restTemplate.postForEntity(baseUrl, mockCommand, PointIssueResponse.class));

        Thread.sleep(200);

        CompletableFuture<ResponseEntity<PointIssueResponse>> future2
                = CompletableFuture.supplyAsync(() -> restTemplate.postForEntity(baseUrl, mockCommand, PointIssueResponse.class));

        ResponseEntity<PointIssueResponse> response3 = restTemplate.postForEntity(baseUrl, mockCommand, PointIssueResponse.class);

        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS); // Bulkhead 기본 상태
        assertThat(response3.getBody()).isNotNull();

        Bulkhead bulkhead = bulkheadRegistry.bulkhead("point-issue-service");
        bulkhead.onComplete();
    }
}