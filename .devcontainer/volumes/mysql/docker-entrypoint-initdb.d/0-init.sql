-- 初始化 SQL 放置说明
-- 1) 当使用外部 SQL（例如 Nacos / 业务库表结构）时，请将 .sql 文件放在本目录下：
--    .devcontainer/volumes/mysql/docker-entrypoint-initdb.d/
--    MySQL 容器首次启动时会按文件名顺序自动执行这些脚本。
-- 2) 如需调整数据库名/账号，请与对应服务的 env 配置保持一致。

-- app
CREATE DATABASE IF NOT EXISTS `radp`;

-- nacos
-- 与 .devcontainer/volumes/nacos/nacos-standalone-mysql.env 保持一致：
--   MYSQL_SERVICE_DB_NAME=nacos_devtest
--   MYSQL_SERVICE_USER=nacos
--   MYSQL_SERVICE_PASSWORD=nacos
CREATE
DATABASE IF NOT EXISTS `nacos_devtest`;
CREATE
USER IF NOT EXISTS 'nacos'@'%' IDENTIFIED BY 'nacos';
GRANT ALL PRIVILEGES ON `nacos_devtest`.* TO
'nacos'@'%';
FLUSH
PRIVILEGES;
