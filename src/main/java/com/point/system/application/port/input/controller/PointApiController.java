package com.point.system.application.port.input.controller;

import com.point.system.application.PointRequestHandler;
import com.point.system.application.dto.*;
import com.point.system.domain.exception.DomainException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/point")
public class PointApiController {

    private final PointRequestHandler pointRequestHandler;

    @PostMapping
    @Bulkhead(name = "point-issue-service", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "fallbackForIssue")
    public CompletableFuture<ResponseEntity<PointIssueResponse> > issue(@RequestBody PointIssueCommand pointIssueCommand) {
        log.info("Point issue: {}", pointIssueCommand.getRequestId());
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(pointRequestHandler.issuePoint(pointIssueCommand)));
    }

    @PostMapping("/retrieve")
    public ResponseEntity<PointRetrieveResponse> retrieve(@RequestBody PointRetrieveCommand command) {
        this.pointRequestHandler.retrievePoint(command);
        return ResponseEntity.ok(new PointRetrieveResponse(command.getRequestId(), "포인트 회수 성공"));
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderCommand command) {
        this.pointRequestHandler.placeOrder(command);
        return ResponseEntity.ok(new OrderResponse(command.getOrderId(), "포인트 사용 성공"));
    }

    @PostMapping("/cancel")
    public ResponseEntity<CancelResponse> cancel(@RequestBody CancelCommand command) {
        this.pointRequestHandler.cancelOrder(command);
        return ResponseEntity.ok(new CancelResponse(command.getOrderId(), "포인트 취소 성공"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlePointInsufficientException(RuntimeException ex) {
        log.warn("Point insufficient error: {}", ex.getMessage());

        // HTTP 400 Bad Request 또는 422 Unprocessable Entity 반환
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    public CompletableFuture<ResponseEntity<PointIssueResponse>> fallbackForIssue(PointIssueCommand command, Throwable t) {
        log.warn("벌크 헤드 허용 요청 초과: {}", command.getRequestId(), t);

        PointIssueResponse fallbackResponse = new PointIssueResponse(command.getRequestId(), null, "허용 용량 초과 에러");
        return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(fallbackResponse)
        );
    }


}
