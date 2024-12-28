# GitLab Runner

## 安装

### docker

```shell
docker run --rm -d --env TZ="Asia/Shanghai" -v "~/.config/gitlab-runner/config:/etc/gitlab-runner" --network local-network gitlab/gitlab-runner:ubuntu-v16.11.4
```

### docker-compose

```yaml
networks:
  local-network:
    driver: bridge

services:
  gitlab-runner:
    image: gitlab/gitlab-runner:ubuntu-v16.11.4
    environment:
      TZ: Asia/Shanghai
    volumes:
      - ~/.config/gitlab-runner/config:/etc/gitlab-runner
    networks:
      - local-network
```

## 注册

### 交互式

```shell
docker run --rm -it -v ~/.config/gitlab-runner/config:/etc/gitlab-runner gitlab/gitlab-runner:ubuntu-v16.11.4 register
```

```plain text
Runtime platform                                    arch=amd64 os=linux pid=6 revision=ac8e767a version=12.6.0
Running in system-mode.

Please enter the gitlab-ci coordinator URL (e.g. https://gitlab.com/):
https://gitlab.com/
Please enter the gitlab-ci token for this runner:
glrt-wpuixyT2fALZK8bf1Vs6
Please enter the gitlab-ci description for this runner:
[00e4f023b5ae]: runner on local docker
Please enter the gitlab-ci tags for this runner (comma separated):
docker-shell
Registering runner... succeeded                     runner=4tutaeWW
Please enter the executor: parallels, virtualbox, docker-ssh+machine, kubernetes, docker+machine, custom, docker, docker-ssh, shell, ssh:
shell
Runner registered successfully. Feel free to start it, but if it's running already the config should be automatically reloaded!
```

### 非交互式

In GitLab 15.10 and later, you create the runner and some of the attributes **in the UI**, like the **tag list**, **locked status**, and **access level**.
In GitLab 15.11 and later, **these attributes are no longer accepted as arguments to register** when a runner authentication token with the `glrt-` prefix is specified.
see https://docs.gitlab.com/ee/ci/runners/new_creation_workflow.html#changes-to-the-gitlab-runner-register-command-syntaxThese

- `--url`: 即私有化部署的 GitLab 地址
- `REDACTED`: 从 GitLab UI 复制

#### 老版本

```shell
docker run --rm -it -v ~/.config/gitlab-runner/config:/etc/gitlab-runner gitlab/gitlab-runner:ubuntu-v16.11.4 register \
  --non-interactive \
  --executor "shell" \
  --url "https://gitlab.com/" \
  --registration-token "REDACTED" \
  --description "runner on local docker" \
  --tag-list "docker-shell" \
  --run-untagged="false" \
  --locked="false" \
  --access-level="not_protected"

```

#### 新版本

老版本中很多参数, 无法再在新版本中使用, 因为它们只能在页面端设定.

```shell
docker run --rm -it -v ~/.config/gitlab-runner/config:/etc/gitlab-runner gitlab/gitlab-runner:ubuntu-v16.11.4 register \
    --non-interactive \
    --executor "shell" \
    --url "https://gitlab.com/" \
    --token "REDACTED"
```