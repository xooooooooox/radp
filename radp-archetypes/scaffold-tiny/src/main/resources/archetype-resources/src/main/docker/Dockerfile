# syntax=docker/dockerfile:1.6

#################### [0] 全局构建参数 ####################
ARG BASE_IMAGE=eclipse-temurin:17-jdk
ARG RUNTIME_BASE_IMAGE=eclipse-temurin:17-jre

ARG APP_NAME=${appName}
ARG RUNTIME_ENV=local
ARG ACTIVE_PROFILE=coding,repo-artifactory
ARG MAVEN_REPO_LOCAL=/root/.m2/repository

ARG BUILD_HOME="/build"
ARG APP_HOME="/workspace"
ARG DATA_HOME="/app/data/$APP_NAME"
ARG LOG_HOME="/app/logs/$APP_NAME"
ARG JAR_FILE=$BUILD_HOME/target/${APP_NAME}.jar
ARG ENTRYPOINT_SCRIPT=src/main/docker/springboot/entrypoint.sh

# 制品库认证(给 .mvn/settings.xml)
ARG ARTIFACTORY_DOMAIN
ARG ARTIFACTORY_USERNAME
ARG ARTIFACTORY_PASSWORD

#################### [1] 基础镜像层优化 阶段 ####################
# 使用 --platform=$BUILDPLATFORM, 与构建机架构保持一致(避免QEMU模拟导致的慢,兼容性或动态库问题)
FROM --platform=$BUILDPLATFORM $BASE_IMAGE AS base
# 安装常用工具，单独缓存层
RUN --mount=type=cache,target=/var/cache/apt,sharing=locked \
	--mount=type=cache,target=/var/lib/apt,sharing=locked \
	apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

#################### [2] 依赖预下载阶段 ####################
FROM base AS deps

# 构建参数
ARG BUILD_HOME
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
WORKDIR "$BUILD_HOME"

# 复制 Maven wrapper
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn/
RUN ([ -x ./mvnw ] || chmod +x ./mvnw)

# 复制配置文件
COPY .coding .coding
COPY .springjavaformatconfig ./

# 复制工程 POM 文件
COPY pom.xml ./

# 3. 下载依赖（利用 Docker 缓存层优化构建速度）
RUN --mount=type=cache,target=$MAVEN_REPO_LOCAL,sharing=locked,id=maven-repo \
  	./mvnw -B \
	--settings .mvn/settings.xml \
	-Dmaven.repo.local=$MAVEN_REPO_LOCAL \
	-P${ACTIVE_PROFILE} \
	dependency:go-offline

######################## [3] 构建阶段 ########################
FROM deps AS builder

ARG APP_NAME
ARG RUNTIME_ENV
ARG MAVEN_REPO_LOCAL
ARG ACTIVE_PROFILE=coding,repo-artifactory,env-${RUNTIME_ENV}

# 4. 复制源代码
COPY src src

# 执行构建
RUN --mount=type=cache,target=$MAVEN_REPO_LOCAL,sharing=locked,id=maven-repo \
	./mvnw -B -ntp \
	--settings .mvn/settings.xml \
	-Dauto.layered.enabled=true \
	-DskipTests \
	-Dmaven.repo.local=$MAVEN_REPO_LOCAL \
	-P${ACTIVE_PROFILE} \
    clean package

#################### [4] JAR 提取阶段 ####################
# 使用基础镜像提取 JAR 分层
FROM --platform=$BUILDPLATFORM $BASE_IMAGE AS jar-extractor

ARG APP_NAME
ARG APP_HOME
ARG BUILD_HOME
ARG JAR_FILE
ARG ENTRYPOINT_SCRIPT

WORKDIR ${APP_HOME}

# 从 Maven 构建阶段复制构建好的 JAR 文件
COPY --from=builder $JAR_FILE application.jar

# 复制启动脚本
COPY $ENTRYPOINT_SCRIPT entrypoint.sh

# 使用 Spring Boot 的分层模式提取 JAR 文件的依赖项
# 1) Use the old tools extractor in Spring Boot 3.1 and below.
# RUN java -Djarmode=layertools -jar application.jar extract
# 2) Use the new tools extractor in Spring Boot 3.2 instead, -Djarmode=layertools deprecated.
RUN java -Djarmode=tools -jar application.jar extract --layers --launcher && \
  rm application.jar

#################### [5] runtime 阶段 ####################
# 创建容器镜像
FROM --platform=$TARGETPLATFORM $RUNTIME_BASE_IMAGE
LABEL org.opencontainers.image.authors="x9x <xozozsos@gmail.com>"

# user
ARG USER=tmpuser
ARG GROUP=tmpgroup
ARG UID=1001
ARG GID=1001
# path
ARG APP_HOME
ARG DATA_HOME
ARG LOG_HOME
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
ARG GC_LOG_PATH="$LOG_HOME"/jvm
ARG GC_LOG_FILE_BASENAME
ARG USE_HEAP_DUMP="Y"
ARG HEAP_DUMP_PATH="$GC_LOG_PATH"
ARG HEAP_DUMP_FILE_BASENAME
ARG USE_LARGE_PAGES="N"
# app
ARG SERVER_PORT="8888"
ARG MANAGEMENT_SERVER_PORT="9999"
ARG SPRING_PROFILES_ACTIVE

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
ENV GC_LOG_FILE_BASENAME=$GC_LOG_FILE_BASENAME
ENV USE_HEAP_DUMP=${USE_HEAP_DUMP}
ENV HEAP_DUMP_PATH="${HEAP_DUMP_PATH}"
ENV HEAP_DUMP_FILE_BASENAME=$HEAP_DUMP_FILE_BASENAME
ENV USE_LARGE_PAGES=${USE_LARGE_PAGES}
ENV SERVER_PORT=${SERVER_PORT}
ENV MANAGEMENT_SERVER_PORT=${MANAGEMENT_SERVER_PORT}
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# 创建系统用户与目录，并准备日志链接
RUN (getent group "$GROUP" || groupadd -g "$GID" "$GROUP") \
  && (getent passwd "$USER" || useradd -u "$UID" -g "$GROUP" -d "$APP_HOME" -m -s /bin/bash "$USER") \
  && echo 'umask 0002' >>"$HOME"/.bashrc \
  && mkdir -p "$APP_HOME" "$DATA_HOME" "$LOG_HOME" "$GC_LOG_PATH" \
  && ln -sf /dev/stdout "$LOG_HOME"/"$HOSTNAME".entrypoint.out \
  && ln -sf /dev/stderr "$LOG_HOME"/"$HOSTNAME".entrypoint.err \
  && chmod -R 755 "$APP_HOME" "$DATA_HOME" "$LOG_HOME" \
  && chown -R "$USER":"$GROUP" "$APP_HOME" "$DATA_HOME" "$LOG_HOME"

WORKDIR $APP_HOME

# 从基础镜像复制应用程序依赖项和模块（直接赋予正确属主，避免额外 chown 层）
# Required Docker Engine >= 17.09
COPY --from=jar-extractor --chown=$UID:$GID "$APP_HOME"/application/spring-boot-loader/ ./
COPY --from=jar-extractor --chown=$UID:$GID "$APP_HOME"/application/dependencies/ ./
COPY --from=jar-extractor --chown=$UID:$GID "$APP_HOME"/application/organization-dependencies/ ./
COPY --from=jar-extractor --chown=$UID:$GID "$APP_HOME"/application/modules-dependencies/ ./
COPY --from=jar-extractor --chown=$UID:$GID "$APP_HOME"/application/snapshot-dependencies/ ./
COPY --from=jar-extractor --chown=$UID:$GID "$APP_HOME"/application/application/ ./
COPY --from=jar-extractor --chown=$UID:$GID --chmod=0755 "$APP_HOME"/entrypoint.sh ./

# 切换到容器用户
USER $USER

# 暴露容器端口
EXPOSE $SERVER_PORT $MANAGEMENT_SERVER_PORT $JDWP_PORT

# 设置启动脚本
ENTRYPOINT ["./entrypoint.sh"]
