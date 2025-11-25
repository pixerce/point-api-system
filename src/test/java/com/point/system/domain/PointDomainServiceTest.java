package com.point.system.domain;

import com.point.system.domain.entity.PointLifespanPolicy;
import com.point.system.domain.entity.PointTransaction;
import com.point.system.domain.entity.PointTypePolicy;
import com.point.system.domain.entity.UserPoint;
import com.point.system.domain.exception.DomainException;
import com.point.system.domain.valueobject.IssueMethod;
import com.point.system.domain.valueobject.StatusType;
import com.point.system.domain.valueobject.TransactionType;
import com.point.system.domain.valueobject.UserPointBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PointDomainServiceTest {

    private static final String ASIA_SEOUL = "Asia/Seoul";

    @InjectMocks
    private PointDomainService pointDomainService;

    @Mock
    private Clock currentZoneClock;

    // 테스트용 ZoneId 및 Clock 설정
    private final ZoneId zoneId = ZoneId.of(ASIA_SEOUL);
    private final Instant fixedInstant = LocalDateTime.of(2025, 11, 26, 10, 0, 0).atZone(zoneId).toInstant();

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("포인트 회수 검증 및 실행 성공")
    void validateAndRetrievePoint_success() {
        UserPoint userPoint = mock(UserPoint.class);
        PointTransaction pointTransaction = mock(PointTransaction.class);

        pointDomainService.validateAndRetrievePoint(userPoint, pointTransaction);

        verify(pointTransaction, times(1)).updateSuccess();
        verify(userPoint, times(1)).validateToRetrieve();
        verify(userPoint, times(1)).initializeToRetrieve();
    }

    @Test
    @DisplayName("포인트 발행 검증 및 초기화 성공")
    void validateAndInitiateIssuePoint_success() {
        UserPoint userPoint = mock(UserPoint.class);
        when(userPoint.getBalance()).thenReturn(100L);
        when(userPoint.getUserPointId()).thenReturn(1L);

        List<PointTypePolicy> typePolicies = List.of(mock(PointTypePolicy.class));
        List<PointLifespanPolicy> lifespanPolicies = List.of(mock(PointLifespanPolicy.class));
        UserPointBalance userPointBalance = mock(UserPointBalance.class);

        pointDomainService.validateAndInitiateIssuePoint(userPoint, typePolicies, lifespanPolicies, userPointBalance);

        verify(userPoint, times(1)).initializeToIssuePoint(currentZoneClock);
        verify(userPoint, times(1)).validatePoint(typePolicies, lifespanPolicies, userPointBalance);
        verify(userPoint, times(1)).addPointTransaction(any(PointTransaction.class));
    }

    @Test
    @DisplayName("포인트 회수 실패 업데이트 성공")
    void updateRetrieveFailed_success() {
        String failureMessage = "잔액 부족 오류";
        PointTransaction pointTransaction = mock(PointTransaction.class);

        pointDomainService.updateRetrieveFailed(failureMessage, pointTransaction);

        verify(pointTransaction, times(1)).updateFailure(failureMessage);
    }

    @Test
    @DisplayName("포인트 사용(주문) 성공")
    void order_success() {
        Long orderAmount = 150L;
        String orderId = "ORDER-20251126";
        UserPointBalance userPointBalance = mock(UserPointBalance.class);
        when(userPointBalance.getTotalBalance()).thenReturn(200L); // 잔액 충분

        UserPoint up1 = mock(UserPoint.class);
        when(up1.getBalance()).thenReturn(100L);
        when(up1.getUserPointId()).thenReturn(1L);
        UserPoint up2 = mock(UserPoint.class);
        when(up2.getBalance()).thenReturn(80L);
        when(up2.getUserPointId()).thenReturn(2L);
        List<UserPoint> userPointList = List.of(up1, up2);

        List<PointTransaction> transactions = pointDomainService.order(orderAmount, orderId, userPointBalance, userPointList);

        assertThat(transactions).hasSize(2);
        verify(up1, times(1)).setBalance(0L);
        verify(up2, times(1)).setBalance(30L); // 80 - 50 = 30

        assertThat(transactions.get(0).getAmount()).isEqualTo(100L);
        assertThat(transactions.get(1).getAmount()).isEqualTo(50L);
    }

    @Test
    @DisplayName("포인트 사용(주문) 실패 - 잔액 부족")
    void order_fail_insufficient_balance() {
        Long orderAmount = 250L;
        String orderId = "ORDER-20251126";
        UserPointBalance userPointBalance = mock(UserPointBalance.class);
        when(userPointBalance.getTotalBalance()).thenReturn(200L);

        List<UserPoint> userPointList = List.of();
        assertThrows(DomainException.class, () ->
                        pointDomainService.order(orderAmount, orderId, userPointBalance, userPointList),
                "잔액 부족 예외가 발생"
        );
    }

    @Test
    @DisplayName("포인트 주문 취소 성공 - 만료되지 않은 포인트")
    void cancelOrder_success_not_expired() {
        String orderId = "ORDER-CANCEL-20251126";
        Long cancelAmount = 70L;

        LocalDateTime now = fixedInstant.atZone(zoneId).toLocalDateTime();
        LocalDateTime futureExpireDate = now.plusDays(10);

        UserPoint up1 = mock(UserPoint.class);
        when(up1.getIssueMethod()).thenReturn(IssueMethod.SYSTEM);
        when(up1.getExpireDate()).thenReturn(futureExpireDate);
        when(up1.getPointTransactionList()).thenReturn(List.of(
                createMockTransaction(TransactionType.BUY, 50L),
                createMockTransaction(TransactionType.CANCEL, 0L)
        ));
        UserPoint up2 = mock(UserPoint.class);
        when(up2.getIssueMethod()).thenReturn(IssueMethod.MANUAL); // 수기 지급이 먼저 정렬
        when(up2.getExpireDate()).thenReturn(futureExpireDate.minusDays(5));
        when(up2.getPointTransactionList()).thenReturn(List.of(
                createMockTransaction(TransactionType.BUY, 100L)
        ));
        when(up2.getBalance()).thenReturn(100L);
        when(up2.getAmount()).thenReturn(100L);

        List<UserPoint> userPointList = new ArrayList<>(List.of(up1, up2));
        List<UserPoint> expiredUserPointList = new ArrayList<>();

        List<PointTransaction> cancelTransactions = pointDomainService.cancelOrder(orderId, cancelAmount, userPointList, expiredUserPointList);

        assertThat(cancelTransactions).hasSize(1);
        assertThat(cancelTransactions.get(0).getAmount()).isEqualTo(70L);
        assertThat(cancelTransactions.get(0).getTransactionType()).isEqualTo(TransactionType.CANCEL);
        assertThat(expiredUserPointList).isEmpty();

        verify(up2, never()).setPointTransactionList(any());
        verify(up1, never()).getPointTransactionList(); // up1까지 반복문이 돌지 않았으므로 호출되면 안됨
    }


    @Test
    @DisplayName("포인트 주문 취소 성공 - 만료된 포인트 포함")
    void cancelOrder_success_with_expired() {
        String orderId = "ORDER-CANCEL-20251126";
        Long cancelAmount = 120L;

        LocalDateTime now = fixedInstant.atZone(zoneId).toLocalDateTime();
        LocalDateTime pastExpireDate = now.minusDays(10);

        UserPoint up1 = mock(UserPoint.class);
        when(up1.getIssueMethod()).thenReturn(IssueMethod.SYSTEM);
        when(up1.getExpireDate()).thenReturn(pastExpireDate); // 만료됨
        when(up1.getPointTransactionList()).thenReturn(List.of(
                createMockTransaction(TransactionType.BUY, 50L) // BUY 50
        ));
        when(up1.getBalance()).thenReturn(0L);
        when(up1.getAmount()).thenReturn(50L);

        UserPoint up2 = mock(UserPoint.class);
        when(up2.getIssueMethod()).thenReturn(IssueMethod.MANUAL);
        when(up2.getExpireDate()).thenReturn(pastExpireDate.minusDays(5));
        when(up2.getPointTransactionList()).thenReturn(List.of(
                createMockTransaction(TransactionType.BUY, 100L) // BUY 100
        ));
        when(up2.getBalance()).thenReturn(0L);
        when(up2.getAmount()).thenReturn(100L); // 총 적립 금액

        List<UserPoint> userPointList = new ArrayList<>(List.of(up1, up2));
        List<UserPoint> expiredUserPointList = new ArrayList<>();

        List<PointTransaction> cancelTransactions = pointDomainService.cancelOrder(orderId, cancelAmount, userPointList, expiredUserPointList);

        assertThat(cancelTransactions).hasSize(2);
        assertThat(cancelTransactions.get(0).getAmount()).isEqualTo(100L); // up2
        assertThat(cancelTransactions.get(1).getAmount()).isEqualTo(20L); // up1

        assertThat(expiredUserPointList).hasSize(2);
        assertThat(expiredUserPointList).contains(up1, up2);

        // 만료된 경우 Issue Transaction 생성 및 목록 설정 검증
        verify(up2, times(1)).setPointTransactionList(any()); // Issue 트랜잭션으로 교체
        verify(up1, times(1)).setPointTransactionList(any()); // Issue 트랜잭션으로 교체
    }

    @Test
    @DisplayName("포인트 주문 취소 실패 - 취소 가능 금액 오류")
    void cancelOrder_fail_cancel_amount_error() {
        String orderId = "ORDER-CANCEL-20251126";
        Long cancelAmount = 160L;

        UserPoint up1 = mock(UserPoint.class);
        when(up1.getPointTransactionList()).thenReturn(List.of(
                createMockTransaction(TransactionType.BUY, 50L)
        ));
        UserPoint up2 = mock(UserPoint.class);
        when(up2.getPointTransactionList()).thenReturn(List.of(
                createMockTransaction(TransactionType.BUY, 100L)
        ));

        List<UserPoint> userPointList = new ArrayList<>(List.of(up1, up2));
        List<UserPoint> expiredUserPointList = new ArrayList<>();

        assertThrows(DomainException.class, () ->
                        pointDomainService.cancelOrder(orderId, cancelAmount, userPointList, expiredUserPointList),
                "취소 가능 금액 오류 예외가 발생"
        );
    }

    private PointTransaction createMockTransaction(TransactionType type, Long amount) {
        return PointTransaction.builder()
                .amount(amount)
                .transactionType(type)
                .status(StatusType.SUCCESS)
                .build();
    }
}