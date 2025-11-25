package com.point.system.data.adapter;

import com.point.system.application.port.output.PointIssueHistoryRepository;
import com.point.system.data.entity.PointIssueHistoryEntity;
import com.point.system.data.mapper.PointIssueHistoryDbMapper;
import com.point.system.data.repository.PointIssueHistoryJpaRepository;
import com.point.system.domain.entity.PointIssueHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PointIssueHistoryRepositoryImpl implements PointIssueHistoryRepository {

    private final PointIssueHistoryDbMapper pointIssueHistoryPersistMapper;
    private final PointIssueHistoryJpaRepository pointIssueHistoryJpaRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PointIssueHistory save(PointIssueHistory pointIssueHistory) {
        PointIssueHistoryEntity pointIssueHistoryEntity = this.pointIssueHistoryPersistMapper.pointIssueHistoryToPointIssueHistoryEntity(pointIssueHistory);
        this.pointIssueHistoryJpaRepository.save(pointIssueHistoryEntity);
        return this.pointIssueHistoryPersistMapper.pointIssueHistoryEntityToPointIssueHistory(pointIssueHistoryEntity);
    }

    @Override
    public Optional<PointIssueHistory> findByRequestId(String requestId) {
        Optional<PointIssueHistoryEntity> optionalPointIssueHistoryEntity = this.pointIssueHistoryJpaRepository.findByRequestId(requestId);
        if (optionalPointIssueHistoryEntity.isPresent())
            return Optional.of(this.pointIssueHistoryPersistMapper.pointIssueHistoryEntityToPointIssueHistory(optionalPointIssueHistoryEntity.get()));
        return Optional.empty();
    }
}
