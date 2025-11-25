CREATE SCHEMA IF NOT EXISTS point;

CREATE TABLE IF NOT EXISTS point.point_type_policy (
    type_policy_id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    point_type VARCHAR DEFAULT 'FREE', -- FREE or CASH
    min_amount INT CHECK (min_amount >= 1) ,
    max_cumulative_amount INT,
    cumulative_period VARCHAR DEFAULT 'TOTAL' , -- YEAR, MONTH
    is_active CHAR DEFAULT 'Y',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS point.point_lifespan_policy (
    lifespan_id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    point_type VARCHAR DEFAULT 'FREE', -- FREE or CASH
    lifespan INT CHECK (lifespan >= 1) ,
    unit VARCHAR DEFAULT 'DAY',
    is_active CHAR DEFAULT 'Y',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS point.user_point (
    user_point_id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    user_id VARCHAR COMMENT '회원 번호' ,
    amount INT NOT NULL CHECK (amount >= 0) COMMENT '지급 금액',
    balance INT NOT NULL DEFAULT 0 CHECK (balance >= 0) COMMENT '잔액',
    point_type VARCHAR DEFAULT 'FREE', -- FREE or CASH
    issue_method VARCHAR DEFAULT 'MANUAL', -- MANUAL or SYSTEM
    start_date DATETIME NOT NULL ,
    expire_date DATETIME NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS point.point_transaction (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    user_point_id BIGINT COMMENT 'user_point 테이블의 PK' ,
    reference_id VARCHAR DEFAULT NULL ,
    amount BIGINT NOT NULL CHECK (amount >= 0) ,
    transaction_type VARCHAR NOT NULL , -- GRANT, RETRIEVE, EXPIRE, BUY, CANCEL
    status VARCHAR ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    failure_reason VARCHAR DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS point.point_issue_history (
    issue_id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    request_id VARCHAR NOT NULL,
    user_point_id BIGINT COMMENT 'user_point 테이블의 PK' ,
    user_id VARCHAR NOT NULL,
    point_type VARCHAR DEFAULT 'FREE', -- FREE or CASH
    amount BIGINT NOT NULL ,
    status VARCHAR DEFAULT NULL , -- SUCCESS, FAILED
    issue_reason VARCHAR DEFAULT NULL ,
    failure_reason VARCHAR DEFAULT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    issue_by VARCHAR DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS point.point_error_log (
    error_id BIGINT PRIMARY KEY AUTO_INCREMENT ,
    reference_id VARCHAR DEFAULT NULL ,
    status VARCHAR DEFAULT NULL ,
    transaction_type VARCHAR NOT NULL , -- GRANT, RETRIEVE, EXPIRE, BUY, CANCEL
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    failure_reason VARCHAR DEFAULT NULL
);





