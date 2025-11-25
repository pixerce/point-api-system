package com.point.system.application.port.output;

import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;

import java.time.ZonedDateTime;

public interface PointErrorLogRepository {

    void log(final String referenceId, StatusType statusType, TransactionType transactionType, String failureReason);
}
