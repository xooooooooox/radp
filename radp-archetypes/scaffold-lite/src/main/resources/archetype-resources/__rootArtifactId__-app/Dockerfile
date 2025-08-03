# syntax=docker/dockerfile:1.6

#################### [0] 全局构建参数 ####################
ARG APP_NAME=${appName}
ARG RUNTIME_ENV=local
ARG ACTIVE_PROFILE=coding,repo-artifactory
ARG MAVEN_REPO_LOCAL=/root/.m2/repository

# 制品库认证(给 .mvn/settings.xml)
ARG ARTIFACTORY_DOMAIN
ARG ARTIFACTORY_USERNAME
ARG ARTIFACTORY_PASSWORD

#################### [1] 基础镜像层优化 阶段 ####################
FROM eclipse-temurin:17-jdk AS base
# 安装常用工具，单独缓存层
RUN --mount=type=cache,target=/var/cache/apt,sharing=locked \
	--mount=type=cache,target=/var/lib/apt,sharing=locked \
	apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

#################### [2] 依赖预下载阶段 ####################
FROM base AS deps

# 构建参数
ARG APP_NAME
ARG ACTIVE_PROFILE
ARG MAVEN_REPO_LOCAL
ARG ARTIFACTORY_DOMAIN
ARG ARTIFACTORY_USERNAME
ARG ARTIFACTORY_PASSWORD

# 设置环境变量
ENV ARTIFACTORY_DOMAIN=${ARTIFACTORY_DOMAIN}
ENV ARTIFACTORY_USERNAME=${ARTIFACTORY_USERNAME}
ENV ARTIFACTORY_PASSWORD=${ARTIFACTORY_PASSWORD}

# 设置工作目录
WORKDIR /build

# 复制 Maven wrapper
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn/
RUN chmod +x ./mvnw

# 复制配置文件
COPY .coding .coding
COPY .springjavaformatconfig ./

# 复制工程 POM 文件
COPY pom.xml ./
COPY ${APP_NAME}-types/pom.xml ${APP_NAME}-types/
COPY ${APP_NAME}-domain/pom.xml ${APP_NAME}-domain/
COPY ${APP_NAME}-infrastructure/pom.xml ${APP_NAME}-infrastructure/
COPY ${APP_NAME}-trigger/pom.xml ${APP_NAME}-trigger/
COPY ${APP_NAME}-app/pom.xml ${APP_NAME}-app/

# 3. 下载依赖（利用 Docker 缓存层优化构建速度）
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
	./mvnw dependency:resolve dependency:resolve-plugins \
	--settings .mvn/settings.xml \
	-Dmaven.repo.local=$MAVEN_REPO_LOCAL \
    -B -ntp \
	-P${ACTIVE_PROFILE}

######################## [3] 构建阶段 ########################
FROM deps AS builder

ARG APP_NAME
ARG RUNTIME_ENV
ARG MAVEN_REPO_LOCAL
ARG ACTIVE_PROFILE=coding,repo-artifactory,env-${RUNTIME_ENV}

# 4. 复制源代码
COPY ${APP_NAME}-types/src ${APP_NAME}-types/src
COPY ${APP_NAME}-domain/src ${APP_NAME}-domain/src
COPY ${APP_NAME}-infrastructure/src ${APP_NAME}-infrastructure/src
COPY ${APP_NAME}-trigger/src ${APP_NAME}-trigger/src
COPY ${APP_NAME}-app/src ${APP_NAME}-app/src

# 执行构建
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
	./mvnw clean package \
	--settings .mvn/settings.xml \
	-Dauto.layered.enabled=true \
	-DskipTests \
	-Dmaven.repo.local=$MAVEN_REPO_LOCAL \
	-B -ntp \
	-P${ACTIVE_PROFILE}

#################### [4] JAR 提取阶段 ####################
# 使用基础镜像提取 JAR 分层
FROM eclipse-temurin:8-jdk AS jar-extractor

ARG APP_NAME

WORKDIR /app

# 从 Maven 构建阶段复制构建好的 JAR 文件
COPY --from=builder /build/${APP_NAME}-app/target/${APP_NAME}.jar application.jar

# 复制启动脚本
COPY ${APP_NAME}-app/src/main/docker/springboot/entrypoint.sh entrypoint.sh

# 使用 Spring Boot 的分层模式提取 JAR 文件的依赖项
# 1) Use the old tools extractor in Spring Boot 3.1 and below.
# RUN java -Djarmode=layertools -jar application.jar extract
# 2) Use the new tools extractor in Spring Boot 3.2 instead, -Djarmode=layertools deprecated.
RUN java -Djarmode=tools -jar application.jar extract --layers --launcher && \
	rm application.jar

#################### [5] runtime 阶段 ####################
# 创建容器镜像
FROM  eclipse-temurin:17-jre
LABEL org.opencontainers.image.authors="x9x <xozozsos@gmail.com>"

# user
ARG USER=tmpuser
ARG GROUP=tmpgroup
ARG UID=1001
ARG GID=1001
# path
ARG APP_HOME="/app"
ARG DATA_HOME="/data"
# zone
ARG TZ="Asia/Shanghai"
ARG LANG="C.UTF-8"
# JVM args
ARG JVM_XMS="1g"
ARG JVM_XMX="1g"
ARG JVM_XSS="256k"
ARG GC_MODE="G1"
# debug & logs
ARG JDWP_DEBUG="N"
ARG JDWP_PORT="5005"
ARG USE_GC_LOG="Y"
ARG GC_LOG_PATH="${DATA_HOME}/logs"
ARG USE_HEAP_DUMP="Y"
ARG HEAP_DUMP_PATH="${DATA_HOME}/logs"
ARG USE_LARGE_PAGES="N"
# app
ARG SERVER_PORT="8888"
ARG MANAGEMENT_SERVER_PORT="9999"
ARG SPRING_PROFILES_ACTIVE="local"

# 设置环境变量
ENV APP_HOME=${APP_HOME}
ENV DATA_HOME=${DATA_HOME}
ENV TZ=${TZ}
ENV LANG=${LANG}
ENV JVM_XMS=${JVM_XMS}
ENV JVM_XMX=${JVM_XMX}
ENV JVM_XSS=${JVM_XSS}
ENV GC_MODE=${GC_MODE}
ENV JDWP_DEBUG=${JDWP_DEBUG}
ENV JDWP_PORT=${JDWP_PORT}
ENV USE_GC_LOG=${USE_GC_LOG}
ENV GC_LOG_PATH="${GC_LOG_PATH}"
ENV USE_HEAP_DUMP=${USE_HEAP_DUMP}
ENV HEAP_DUMP_PATH="${HEAP_DUMP_PATH}"
ENV USE_LARGE_PAGES=${USE_LARGE_PAGES}
ENV SERVER_PORT=${SERVER_PORT}
ENV MANAGEMENT_SERVER_PORT=${MANAGEMENT_SERVER_PORT}
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# 创建普通用户并切换工作目录(包括设置 umask)
RUN groupadd -g "$GID" "$GROUP" \
  && useradd -u "$UID" -g "$GROUP" -d "$APP_HOME" -m -s /bin/bash "$USER" \
  && echo 'umask 0002' >>"$APP_HOME"/.bashrc \
  && chmod -R 755 "$APP_HOME" \
  && mkdir -p "$DATA_HOME" \
  && chown -R "$USER":"$GROUP" "$DATA_HOME" \
  && chmod -R 755 "$DATA_HOME"

WORKDIR $APP_HOME

# 从基础镜像复制应用程序依赖项和模块
COPY --from=jar-extractor /app/application/spring-boot-loader/ ./
COPY --from=jar-extractor /app/application/dependencies/ ./
COPY --from=jar-extractor /app/application/organization-dependencies/ ./
COPY --from=jar-extractor /app/application/modules-dependencies/ ./
COPY --from=jar-extractor /app/application/snapshot-dependencies/ ./
COPY --from=jar-extractor /app/application/application/ ./
COPY --from=jar-extractor /app/entrypoint.sh ./
RUN chmod +x "$APP_HOME"/entrypoint.sh

# 切换到容器用户并创建日志目录
USER $USER
RUN mkdir -p "$DATA_HOME"/logs \
  && touch "$DATA_HOME"/logs/entrypoint.out \
  && ln -sf /dev/stdout "$DATA_HOME"/logs/entrypoint.out \
  && ln -sf /dev/stderr "$DATA_HOME"/logs/entrypoint.out

# 设置启动入口和端口
EXPOSE $SERVER_PORT $MANAGEMENT_SERVER_PORT
ENTRYPOINT ["./entrypoint.sh"]
