# MySQL 初始化 SQL 放置说明

当需要“使用外部 SQL（如业务库表结构、第三方组件表结构）”时，请将对应的初始化 SQL 文件放到当前目录：

.devcontainer/volumes/mysql/docker-entrypoint-initdb.d/

该目录下的 .sql 文件会在 MySQL 容器首次初始化（数据目录为空）时，按文件名的字典序依次自动执行。建议使用数字前缀控制执行顺序，例如：

- 0-init.sql —— 创建数据库、用户、基础权限
- 1-app-schema.sql —— 导入业务库表结构/数据
- 2-xxx.sql —— 其他初始化脚本

注意事项：

- 脚本请尽量具备幂等性（例如使用 CREATE IF NOT EXISTS / DROP IF EXISTS 等），便于重复执行或恢复。
- 如需创建用户/授权，需在该目录中以 root 权限执行的 SQL 中完成（容器首次初始化阶段会以 root 连接执行）。
- 仅首次启动并初始化数据目录时会自动执行。之后若需再次执行，请清空 MySQL 数据目录或手动执行脚本。

与 Nacos 的对应关系（示例）：

- Nacos 的环境变量位于：.devcontainer/volumes/nacos/nacos-standalone-mysql.env
- 关键项：
  - MYSQL_SERVICE_DB_NAME=nacos_devtest
  - MYSQL_SERVICE_USER=nacos
  - MYSQL_SERVICE_PASSWORD=nacos
- 本目录下的 0-init.sql 已示例性地：
  - 创建数据库 nacos_devtest
  - 创建用户 `nacos` 并授予 `nacos_devtest.*` 全部权限

业务库示例：

- 如果你的业务库名为 scaffold-std-demo，可在 1-app-schema.sql 中导入该库的表结构和基础数据：
  - USE `scaffold-std-demo`;
  - -- 后续表结构/数据...

提示：

- 若引入其他组件（如 xxl-job 等），请将其官方提供的建库/建表 SQL 同样放入本目录，并根据需要调整执行顺序。

附注（Nacos 初始化脚本来源与版本匹配）：

- 本目录中的 1-nacos-schema.sql 参考并改写自官方仓库，以便在 MySQL 首次初始化时自动创建 Nacos 所需的表与默认数据。
- 上游参考链接（示例）：https://github.com/nacos-group/nacos-docker/blob/master/example/image/mysql/8-m1/Dockerfile
- 如后续升级 Nacos 版本，请留意上游 schema 变更，必要时同步更新本文件以保持兼容。
