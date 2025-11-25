package com.point.system.data.adapter;

import com.point.system.application.port.output.PointErrorLogRepository;
import com.point.system.data.entity.PointErrorLogEntity;
import com.point.system.data.repository.PointErrorLogJpaRepository;
import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PointErrorLogRepositoryImpl implements PointErrorLogRepository {

    private final PointErrorLogJpaRepository pointErrorLogJpaRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String referenceId, StatusType statusType, TransactionType transactionType, String failureReason) {

        PointErrorLogEntity errorLogEntity = PointErrorLogEntity.builder()
                .referenceId(referenceId)
                .status(statusType)
                .transactionType(transactionType)
                .failureReason(failureReason)
                .build();

        this.pointErrorLogJpaRepository.save(errorLogEntity);
    }
}
