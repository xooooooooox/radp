CREATE TABLE raffle_activity
(
    id                  BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '自增 ID',
    activity_id         BIGINT(12)   NOT NULL COMMENT '活动ID',
    activity_name       VARCHAR(64)  NOT NULL COMMENT '活动名称',
    activity_desc       VARCHAR(128) NOT NULL COMMENT '活动描述',
    activity_start_time DATETIME     NOT NULL COMMENT '活动开始时间',
    activity_end_time   DATETIME     NOT NULL COMMENT '活动结束时间',
    created_date        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_modified_date  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uq_activity_id (activity_id),
    KEY idx_activity_start_time (activity_start_time),
    KEY idx_activity_end_time (activity_end_time)
) COMMENT '抽奖活动表';