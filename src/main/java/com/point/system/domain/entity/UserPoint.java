package com.point.system.domain.entity;

import com.point.system.domain.component.LifespanCalculatorFactory;
import com.point.system.domain.component.LifespanValidatorFactory;
import com.point.system.domain.exception.DomainException;
import com.point.system.domain.valueobject.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class UserPoint {

    private Long balance;
    private Long userPointId;
    private String userId;
    private Long amount;
    private PointType pointType;
    private IssueMethod issueMethod;
    private LocalDateTime startDate;
    private LocalDateTime expireDate;

    private Integer lifespan;
    private LifespanUnit lifespanUnit;

    private List<PointTransaction> pointTransactionList = new ArrayList<>();

    private UserPoint(Builder builder) {
        setBalance(builder.balance);
        setUserPointId(builder.userPointId);
        setUserId(builder.userId);
        setAmount(builder.amount);
        setPointType(builder.pointType);
        setIssueMethod(builder.issueMethod);
        setStartDate(builder.startDate);
        setExpireDate(builder.expireDate);
        setLifespan(builder.lifespan);
        setLifespanUnit(builder.lifespanUnit);
        if (builder.pointTransactionList != null)
            setPointTransactionList(new ArrayList<>(builder.pointTransactionList));
    }

    public void addPointTransaction(PointTransaction pointTransaction) {
        this.pointTransactionList.add(pointTransaction);
    }

    public void validateToRetrieve() {
        if (!Objects.equals(this.amount, this.balance))
            throw new DomainException("금액 중 일부를 사용하여 적립 취소 불가");
    }

    public void initializeToRetrieve() {
        PointTransaction pointTransaction = createPointTransaction(TransactionType.RETRIEVE);
        pointTransaction.updateSuccess();
        this.pointTransactionList.add(pointTransaction);
        this.balance = 0L;
    }

    private PointTransaction createPointTransaction(TransactionType transactionType) {
        return PointTransaction.builder()
                .amount(this.getBalance())
                .transactionType(transactionType)
                .userPointId(this.getUserPointId())
                .build();
    }

    public void validatePoint(List<PointTypePolicy> pointTypePolicyList, List<PointLifespanPolicy> pointLifespanPolicyList, UserPointBalance userPointBalance) {
        validateMinAndMaxAmount(pointTypePolicyList);
        validateCumulativePoint(pointTypePolicyList, userPointBalance);
        validateLifespan(pointLifespanPolicyList);
    }

    public void validateLifespan(List<PointLifespanPolicy> pointLifespanPolicyList) {
        pointLifespanPolicyList
                .stream().filter(policy -> policy.getUnit() == this.lifespanUnit)
                .forEach(pointLifespanPolicy -> {
            LifespanValidatorFactory.createLifespanUnit(pointLifespanPolicy.getUnit())
                    .validate(this.startDate, this.expireDate, this.lifespan);
        });
    }

    public void validateCumulativePoint(List<PointTypePolicy> pointTypePolicyList, UserPointBalance userPointBalance) {

        for (PointTypePolicy pointTypePolicy : pointTypePolicyList) {
            if (pointTypePolicy.getCumulativePeriod() == CumulativePeriod.TOTAL
                    && pointTypePolicy.getPoinType() == this.pointType) {
                if (this.amount + userPointBalance.getTotalBalance() > pointTypePolicy.getMaxCumulativeAmount())
                    throw new DomainException("보유 가능한 포인트 최대 한도 초과");
            }
        }
    }

    private void validateMinAndMaxAmount(List<PointTypePolicy> pointTypePolicyList) {
        Optional<PointTypePolicy> optionalPointTypePolicy = pointTypePolicyList.stream()
                .filter(policy -> policy.getPoinType() == this.pointType)
                .filter(policy -> policy.getCumulativePeriod() == CumulativePeriod.ONCE)
                .findAny();

        PointTypePolicy pointTypePolicy = optionalPointTypePolicy.orElseThrow(() -> new DomainException("지급 정책 없음"));
        if (this.amount < pointTypePolicy.getMinAmount())
            throw new DomainException("지급 가능한 최저 금액 오류");
        if (this.amount > pointTypePolicy.getMaxCumulativeAmount())
            throw new DomainException("지급 가능한 최대 금액 오류");
    }

    public void initializeToIssuePoint(Clock currentZoneClock) {
        this.startDate = LocalDateTime.now(currentZoneClock);
        this.expireDate = LifespanCalculatorFactory.createLifespanUnit(this.lifespanUnit)
                .calculate(this.startDate, this.lifespan);

        this.balance = this.amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long balance;
        private Long userPointId;
        private String userId;
        private Long amount;
        private PointType pointType;
        private IssueMethod issueMethod;
        private LocalDateTime startDate;
        private LocalDateTime expireDate;
        private List<PointTransaction> pointTransactionList;

        private Integer lifespan;
        private LifespanUnit lifespanUnit;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder balance(Long val) {
            balance = val;
            return this;
        }

        public Builder userPointId(Long val) {
            userPointId = val;
            return this;
        }

        public Builder userId(String val) {
            userId = val;
            return this;
        }

        public Builder amount(Long val) {
            amount = val;
            return this;
        }

        public Builder pointType(PointType val) {
            pointType = val;
            return this;
        }

        public Builder issueMethod(IssueMethod val) {
            issueMethod = val;
            return this;
        }

        public Builder startDate(LocalDateTime val) {
            startDate = val;
            return this;
        }

        public Builder expireDate(LocalDateTime val) {
            expireDate = val;
            return this;
        }

        public Builder pointTransactionList(List<PointTransaction> val) {
            pointTransactionList = val;
            return this;
        }

        public Builder lifespan(Integer val) {
            lifespan = val;
            return this;
        }

        public Builder lifespanUnit(LifespanUnit val) {
            lifespanUnit = val;
            return this;
        }

        public UserPoint build() {
            return new UserPoint(this);
        }
    }
}
