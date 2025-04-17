# 使用基础镜像
FROM eclipse-temurin:17-jdk  AS builder

# 设置工作目录并指定构建模块
WORKDIR /app
ARG MODULE=.
ARG ARTIFACT=${appName}.jar

# 复制必要文件
COPY ${MODULE}/target/$ARTIFACT application.jar
COPY ${MODULE}/src/main/docker/springboot/entrypoint.sh entrypoint.sh

# 使用 Spring Boot 的分层模式提取 JAR 文件的依赖项
# 1) Use the old tools extractor in Spring Boot 3.1 and below.
# RUN java -Djarmode=layertools -jar application.jar extract
# 2) Use the new tools extractor in Spring Boot 3.2 instead, -Djarmode=layertools deprecated.
RUN java -Djarmode=tools -jar application.jar extract --layers --launcher


# 创建容器镜像
FROM  eclipse-temurin:17-jdk
LABEL org.opencontainers.image.authors="x9x <xozozsos@gmail.com>"

ARG USER=tmpuser
ARG GROUP=tmpgroup
ARG UID=1001
ARG GID=1001

ENV APP_HOME="/app"
ENV DATA_HOME="/data"
ENV TZ="Asia/Shanghai"
ENV LANG="C.UTF-8"
ENV JVM_XMS="1g"
ENV JVM_XMX="1g"
ENV JVM_XSS="256k"
ENV JVM_GC_MODE="G1"
ENV JDWP_DEBUG="N"
ENV JDWP_PORT="5005"
ENV USE_GC_LOG="Y"
ENV GC_LOG_PATH="${DATA_HOME}/logs"
ENV USE_HEAP_DUMP="Y"
ENV HEAP_DUMP_PATH="${DATA_HOME}/logs"
ENV USE_LARGE_PAGES="N"
ENV SERVER_PORT="8888"
ENV MANAGEMENT_SERVER_PORT="9999"
ENV SPRING_PROFILES_ACTIVE="dev"

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
COPY --from=builder /app/application/spring-boot-loader ./
COPY --from=builder /app/application/dependencies/ ./
COPY --from=builder /app/application/organization-dependencies ./
COPY --from=builder /app/application/modules-dependencies ./
COPY --from=builder /app/application/snapshot-dependencies/ ./
COPY --from=builder /app/application/application/ ./
COPY --from=builder /app/entrypoint.sh ./
RUN chmod +x entrypoint.sh

# 切换到容器用户并创建日志目录
USER $USER
RUN mkdir -p "$DATA_HOME"/logs \
  && touch "$DATA_HOME"/logs/entrypoint.out \
  && ln -sf /dev/stdout "$DATA_HOME"/logs/entrypoint.out \
  && ln -sf /dev/stderr "$DATA_HOME"/logs/entrypoint.out

# 设置启动入口和端口
EXPOSE $SERVER_PORT $MANAGEMENT_SERVER_PORT
ENTRYPOINT ["./entrypoint.sh"]
