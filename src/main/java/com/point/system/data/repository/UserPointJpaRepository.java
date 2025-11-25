package com.point.system.data.repository;

import com.point.system.data.entity.UserPointEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPointJpaRepository extends JpaRepository<UserPointEntity, Long>, UserPointJpaRepositoryCustom {

    @EntityGraph(attributePaths = "pointTransactionList")
    List<UserPointEntity> findAll();

    @EntityGraph(attributePaths = "pointTransactionList")
    Optional<UserPointEntity> findById(Long id);

    @Query(value = """
            SELECT t.user_point_id, t.balance, t.amount, t.expire_date, t.created_at, t.updated_at, t.issue_method, t.point_type, t.start_date, t.user_id
                    FROM (
                        SELECT
                            user_point_id, balance, issue_method, expire_date, created_at, updated_at, amount, point_type, start_date, user_id,
                            SUM(balance) OVER(ORDER BY issue_method, expire_date) AS running_sum
                        FROM
                            point.user_point
                        WHERE user_id = ?1 
                        AND start_date < now() AND now() < expire_date
                        AND balance > 0
                    ) AS t
                    WHERE t.running_sum - t.balance < ?2
                    ORDER BY t.issue_method, t.expire_date
        """, nativeQuery = true)
    List<UserPointEntity> getAvailablePointList(String userId, Long amount);

    @Override
    @EntityGraph(attributePaths = "pointTransactionList")
    List<UserPointEntity> findAllById(Iterable<Long> longs);
}
