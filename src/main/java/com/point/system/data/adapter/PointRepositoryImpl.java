package com.point.system.data.adapter;

import com.point.system.application.port.output.PointRepository;
import com.point.system.data.entity.PointTransactionEntity;
import com.point.system.data.entity.UserPointEntity;
import com.point.system.data.mapper.PointDbMapper;
import com.point.system.data.repository.UserPointJpaRepository;
import com.point.system.domain.entity.PointTransaction;
import com.point.system.domain.entity.UserPoint;
import com.point.system.domain.exception.DomainException;
import com.point.system.domain.valueobject.PointType;
import com.point.system.domain.valueobject.UserPointBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class PointRepositoryImpl implements PointRepository {

    private final UserPointJpaRepository userPointJpaRepository;
    private final PointDbMapper pointDbMapper;

    @Override
    public UserPoint getUserPoint(Long userPointId) {
        Optional<UserPointEntity> optionalUserPointEntity = this.userPointJpaRepository.findById(userPointId);
        if (optionalUserPointEntity.isEmpty())
            throw new DomainException("포인트 미존재");

        return this.pointDbMapper.userPointEntityToUserPoint(optionalUserPointEntity.get());
    }

    @Override
    public void save(UserPoint userPoint) {
        UserPointEntity userPointEntity = this.pointDbMapper.userPointToUserPointEntity(userPoint);
        this.userPointJpaRepository.save(userPointEntity);
    }

    @Override
    public UserPointBalance getUserPointBalance(String userId, PointType pointType) {
        return userPointJpaRepository.getTotalBalance(userId, pointType);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveRetrievePoint(UserPoint userPoint, PointTransaction pointTransaction) {
        Optional<UserPointEntity> optionalUserPointEntity = this.userPointJpaRepository.findById(userPoint.getUserPointId());
        if (optionalUserPointEntity.isEmpty())
            throw new DomainException("포인트 미존재");

        UserPointEntity userPointEntity = optionalUserPointEntity.get();
        userPointEntity.setBalance(userPoint.getBalance());
        userPointEntity.addPointTransaction(this.pointDbMapper.pointTransactionToPointTransactionEntity(pointTransaction));
        this.userPointJpaRepository.save(userPointEntity);
    }

    @Override
    public List<UserPoint> getAvailablePointList(String userId, Long amount) {
        List<UserPointEntity> userPointEntityList = this.userPointJpaRepository.getAvailablePointList(userId, amount);
        return userPointEntityList.stream()
                .map(this.pointDbMapper::userPointEntityToUserPointWithoutPointTransaction)
                .toList();
    }

    @Transactional
    @Override
    public void persistOrder(List<PointTransaction> pointTransactionList) {
        List<Long> userPointIdList = pointTransactionList.stream().map(PointTransaction::getUserPointId).collect(Collectors.toUnmodifiableList());
        List<UserPointEntity> userPointEntityList = this.userPointJpaRepository.findAllById(userPointIdList);
        userPointEntityList.forEach(userPointEntity -> {
            PointTransaction pointTransaction = pointTransactionList.stream().filter(pt -> pt.getUserPointId() == userPointEntity.getUserPointId()).findAny().get();
            userPointEntity.setBalance(userPointEntity.getBalance() - pointTransaction.getAmount());
            userPointEntity.addPointTransaction(this.pointTransactionToPointTransactionEntity(pointTransaction));
        });
        this.userPointJpaRepository.saveAll(userPointEntityList);
    }

    private PointTransactionEntity pointTransactionToPointTransactionEntity(PointTransaction pointTransaction) {
        return PointTransactionEntity.builder()
                .transactionType(pointTransaction.getTransactionType())
                .amount(pointTransaction.getAmount())
                .status(pointTransaction.getStatus())
                .referenceId(pointTransaction.getReferenceId())
                .failureReason(pointTransaction.getFailureReason())
                .build();
    }

    public List<UserPoint> findUserPointListByOrderId(String orderId) {
        List<UserPointEntity> userPointEntityList = this.userPointJpaRepository.findUserPointList(orderId);

        return userPointEntityList.stream()
                .map(this.pointDbMapper::userPointEntityToUserPoint)
                .toList();
    }

    @Transactional
    @Override
    public void persistCancel(List<PointTransaction> pointTransactionList) {
        List<Long> userPointIdList = pointTransactionList.stream().map(PointTransaction::getUserPointId).collect(Collectors.toUnmodifiableList());
        List<UserPointEntity> userPointEntityList = this.userPointJpaRepository.findAllById(userPointIdList);
        userPointEntityList.forEach(userPointEntity -> {
            PointTransaction pointTransaction = pointTransactionList.stream().filter(pt -> Objects.equals(pt.getUserPointId(), userPointEntity.getUserPointId())).findAny().get();
            userPointEntity.setBalance(userPointEntity.getBalance() + pointTransaction.getAmount());
            userPointEntity.addPointTransaction(this.pointTransactionToPointTransactionEntity(pointTransaction));
        });
        this.userPointJpaRepository.saveAll(userPointEntityList);
    }
}
