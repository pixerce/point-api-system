package com.point.system.domain.valueobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public class UserPointBalance {
    private final Long totalBalance;

    public UserPointBalance(Long totalBalance) {
        this.totalBalance = (totalBalance == null) ? 0 : totalBalance;
    }
}
