package com.point.system.domain.event;

import com.point.system.domain.entity.PointTransaction;
import com.point.system.domain.event.valueobject.PointEventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@RequiredArgsConstructor
@Getter
@Setter
public class PointIssueEvent implements BaseEvent {
    private final List<PointTransaction> pointTransactionList;
    private final PointEventType pointEventType;
}
