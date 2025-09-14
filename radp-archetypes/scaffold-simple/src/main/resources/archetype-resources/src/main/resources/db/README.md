## changelog-init.yaml

```yaml
databaseChangeLog:
  # ---------------------- 通用（所有环境都会执行） ----------------------
  - changeSet:
      id: init
      author: x9x
      comment: 初始化
      changes:
        - sqlFile:
            relativeToChangelogFile: true
            path: ../migration/init/ddl/changes-ddl.sql
            stripComments: true
        - sqlFile:
            relativeToChangelogFile: true
            path: ../migration/init/dml/changes-dml.sql
        - loadData:
            relativeToChangelogFile: true
            file: ../migration/init/dml/demo.csv
            tableName: demo
            separator: ;
            columns:
              - column:
                  name: name
                  type: varchar
              - column:
                  name: locked
                  type: boolean
      rollback:
        - sqlFile:
            relativeToChangelogFile: true
            path: ../migration/init/rollback/rollback.sql
            stripComments: true

  # Example of a second changeSet in the same file
  - changeSet:
      id: 20241018-001
      author: x9x
      comment: 示例第二个changeSet
      changes:
        - sql:
            sql: SELECT 1

  # ---------------------- 仅 dev / uat 环境执行 ------------------------
  - changeSet:
      id: 20241018-003
      author: x9x
      comment: 导入演示数据（仅 dev、uat）
      context: dev,uat           # <- 限定环境
      changes:
        - loadData:
            relativeToChangelogFile: true
            file: ../migration/init/env/demo_seed_data.csv
            tableName: demo
            separator: ;
            columns:
              - column: { name: name,   type: varchar }
              - column: { name: locked, type: boolean }
      rollback:
        - delete:
            tableName: demo         # 回滚时删除刚插入的数据
            where: "locked = FALSE" # 仅删演示数据，避免误删正式数据

  # ---------------------- 仅 prod 环境执行 ------------------------------
  - changeSet:
      id: 20241018-004
      author: x9x
      comment: 为 prod 新建分区或索引
      context: prod               # <— 只在生产库跑
      runAlways: false            # prod 里改结构通常只跑一次
      changes:
        - sqlFile:
            relativeToChangelogFile: true
            path: ../migration/init/env/prod/create_prod_index.sql
      rollback:
        - sqlFile:
            relativeToChangelogFile: true
            path: ../migration/init/env/prod/rollback/drop_prod_index.sql

```
