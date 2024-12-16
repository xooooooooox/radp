CREATE TABLE `demo`
(
    `id`     BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`   VARCHAR(20)         DEFAULT NULL COMMENT '姓名',
    `locked` BIT(1)     NOT NULL DEFAULT 0 COMMENT '是否锁定(1: 锁定, 0: 未锁定)',
    PRIMARY KEY (`id`)
) COMMENT = '示例表';