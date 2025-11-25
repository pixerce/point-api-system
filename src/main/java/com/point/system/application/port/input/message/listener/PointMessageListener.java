package com.point.system.application.port.input.message.listener;

import com.point.system.application.PointRequestHandler;
import com.point.system.domain.event.PointIssueEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class PointMessageListener {

    private final PointRequestHandler pointRequestHandler;

    @Async
    @TransactionalEventListener
    public void handlePointIssueEvent(PointIssueEvent pointIssueEvent) {
        System.out.println("포인트 발급 이벤트 수신");
        System.out.println(pointIssueEvent);

    }
}
