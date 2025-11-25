package com.point.system.domain.entity;

import com.point.system.domain.valueobject.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class PointPolicy {
    private final Long policyId;
    private final Long amount;
    private final PointType pointType;
    private final IssueMethod issueMethod;
    private final LocalDateTime startDate;
    private final Integer lifeSpan;

    private List<PointTypePolicy> pointTypePolicyList;


}
