########################################################################################################################
# 每张表一般至少需要包含以下字段
/*
-- ----------------------------
-- Table structure for example
-- ----------------------------
DROP TABLE IF EXISTS `example`;
CREATE TABLE `example`
(
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    creator         VARCHAR(64)  NULL     DEFAULT NULL COMMENT '创建者',
    updater         VARCHAR(64)  NULL     DEFAULT NULL COMMENT '更新者',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    deleted         BIT(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
    tenant_id       BIGINT       NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (id)
) COMMENT '示例表';
*/
########################################################################################################################

########################################################################################################################
