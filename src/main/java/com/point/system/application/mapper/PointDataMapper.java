package com.point.system.application.mapper;

import com.point.system.application.dto.PointIssueCommand;
import com.point.system.domain.entity.PointIssueHistory;
import com.point.system.domain.entity.UserPoint;
import com.point.system.domain.valueobject.StatusType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.point.system.common.Constants.ASIA_SEOUL;

@Component
public class PointDataMapper {

    public UserPoint issuePointCommandToUserPoint(PointIssueCommand issuePointCommand) {
        return UserPoint.builder()
                .userId(issuePointCommand.getUserId())
                .amount(issuePointCommand.getAmount())
                .issueMethod(issuePointCommand.getIssueMethod())
                .pointType(issuePointCommand.getPointType())
                .lifespan(issuePointCommand.getLifeSpan())
                .lifespanUnit(issuePointCommand.getLifespanUnit())
                .build();

    }

    public PointIssueHistory createPointIssueHistory(PointIssueCommand pointIssueCommand) {
        return PointIssueHistory.builder()
                .requestId(pointIssueCommand.getRequestId())
                .userId(pointIssueCommand.getUserId())
                .pointType(pointIssueCommand.getPointType())
                .amount(pointIssueCommand.getAmount())
                .status(StatusType.SUCCESS)
                .issueBy(pointIssueCommand.getIssueBy())
                .issueReason(pointIssueCommand.getIssueReason())
                .createdAt(LocalDateTime.now(ZoneId.of(ASIA_SEOUL)))
                .updatedAt(LocalDateTime.now(ZoneId.of(ASIA_SEOUL)))
                .build();
    }


}
