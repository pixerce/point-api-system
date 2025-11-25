package com.point.system.application.dto;

import com.point.system.domain.valueobject.IssueMethod;
import com.point.system.domain.valueobject.LifespanUnit;
import com.point.system.domain.valueobject.PointType;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@Builder
@NoArgsConstructor
public class PointIssueCommand {
    private String requestId;
    private String userId;
    private PointType pointType;
    private IssueMethod issueMethod;
    private Integer lifeSpan;
    private LifespanUnit lifespanUnit;
    private Long amount;
    private String pointName;
    private String issueBy;
    private String issueReason;

}
