#!/bin/bash
# Fail on error, undefined variables, and pipe failures
set -euo pipefail

# Detect if Java is installed and available in PATH
_detect_java_environment() {
  if ! command -v java >/dev/null 2>&1; then
    echo "Error: could not find java in PATH"
    return 1
  fi
}

# Add a JVM option if it's supported by the current Java version
_append_if_supported() {
  local raw_opt="$1"

  # ----------- 1) Process options starting with -XX: -----------
  # 处理 -XX: 开头（同前）
  if [[ $raw_opt == -XX:* ]]; then # e.g. -XX:+UseStringDeduplication
    # Extract flag name by removing -XX: prefix and any +/- or =xxx
    # 提取 flag 名称去掉 -XX: 前缀和任何 +/- 或 =xxx
    local flag="${raw_opt#-XX:}" # Remove prefix
    flag="${flag#[-+=]}"         # Remove first +, -, or =
    flag="${flag%%=*}"           # Remove everything after first =

    # JVM flags must be unlocked first, otherwise they won't be printed
    # JVM flags 一定要先解锁，否则不会 Print 出来
    local flags_output
    flags_output=$(java -XX:+UnlockExperimentalVMOptions \
      -XX:+UnlockDiagnosticVMOptions \
      -XX:+PrintFlagsFinal -version 2>&1)
    if grep -q -E "^[[:space:]]+[[:alnum:]_]+[[:space:]]+${flag}[[:space:]]" <<<"${flags_output}"; then
      JAVA_OPTS+=" ${raw_opt}"
    else
      echo "Skip unsupported VM option: ${raw_opt}"
    fi

  # ----------- 2) Process options starting with -X -----------
  # 处理 -X 开头
  elif [[ $raw_opt == -X* ]]; then # e.g. -Xloggc:...
    # Extract "template name" by removing numeric values and everything after colon
    # 拆出 “模板名”：去掉数值等尾巴（第一个数字/冒号后全部切掉）
    local tmpl="${raw_opt%%[0-9]*}" # Remove digits and everything after
    tmpl="${tmpl%%:*}"              # Remove colon and everything after

    # java -X list includes template formats like -Xms<size>
    # java -X 列表包含 -Xms<size> 这种模板写法
    if java -X 2>&1 | grep -q -- "${tmpl}"; then
      JAVA_OPTS+=" ${raw_opt}"
    else
      echo "Skip unsupported -X option: ${raw_opt}"
    fi

  # ----------- 3) Add other parameters directly -----------
  # 其它普通参数直接加
  else
    # Directly append normal parameters like -Dxxx
    # 普通 -Dxxx 或其他运行参数直接追加
    JAVA_OPTS+=" ${raw_opt}"
  fi
}

# Optimize Java options based on environment variables and Java version
_optimize_java_opts() {
  # Get Java major version
  # 获取 Java 主版本
  # More robust version detection that works with different Java version formats
  JAVA_MAJOR_VERSION=$(java -version 2>&1 | sed -E -n 's/.* version "([^.-]*).*$/\1/p')
  if [[ -z "${JAVA_MAJOR_VERSION}" ]]; then
    # Try an alternative format for newer Java versions
    JAVA_MAJOR_VERSION=$(java -version 2>&1 | sed -E -n 's/^(java|openjdk) version "([0-9]+).*$/\2/p')
    if [[ -z "${JAVA_MAJOR_VERSION}" ]]; then
      echo "Warning: Could not determine Java version, assuming Java 8"
      JAVA_MAJOR_VERSION="8"
    fi
  fi
  java -version || exit 1
  echo "Detected Java major version: ${JAVA_MAJOR_VERSION}"

  # Default Java parameter settings
  # 默认 Java 参数设置
  _append_if_supported "-server"
  _append_if_supported "-XX:+UnlockExperimentalVMOptions"
  _append_if_supported "-XX:+UnlockDiagnosticVMOptions"
  _append_if_supported "-XX:+AlwaysPreTouch"
  _append_if_supported "-XX:+PrintFlagsFinal"
  _append_if_supported "-XX:-DisplayVMOutput"
  _append_if_supported "-XX:-OmitStackTraceInFastThrow"
  _append_if_supported "-Xms${JVM_XMS:-1G}"
  _append_if_supported "-Xmx${JVM_XMX:-1G}"
  _append_if_supported "-Xss${JVM_XSS:-256K}"
  _append_if_supported "-XX:MetaspaceSize=${METASPACE_SIZE:-128M}"
  _append_if_supported "-XX:MaxMetaspaceSize=${MAX_METASPACE_SIZE:-256M}"
  _append_if_supported "-XX:MaxGCPauseMillis=${MAX_GC_PAUSE_MILLIS:-200}"

  # Garbage collection mode settings
  # 垃圾回收模式设置
  if [[ "${GC_MODE:-}" == "ShenandoahGC" ]]; then
    echo "GC mode is ShenandoahGC"
    _append_if_supported "-XX:+UseShenandoahGC"
  elif [[ "${GC_MODE:-}" == "ZGC" ]]; then
    echo "GC mode is ZGC"
    _append_if_supported "-XX:+UseZGC"
  elif [[ "${GC_MODE:-}" == "G1" ]]; then
    echo "GC mode is G1"
    _append_if_supported "-XX:+UseG1GC"
    _append_if_supported "-XX:InitiatingHeapOccupancyPercent=${INITIATING_HEAP_OCCUPANCY_PERCENT:-45}"
    _append_if_supported "-XX:G1ReservePercent=${G1_RESERVE_PERCENT:-10}"
    _append_if_supported "-XX:G1HeapWastePercent=${G1_HEAP_WASTE_PERCENT:-5}"
    _append_if_supported "-XX:G1NewSizePercent=${G1_NEW_SIZE_PERCENT:-50}"
    _append_if_supported "-XX:G1MaxNewSizePercent=${G1_MAX_NEW_SIZE_PERCENT:-60}"
    _append_if_supported "-XX:G1MixedGCCountTarget=${G1_MIXED_GCCOUNT_TARGET:-8}"
    _append_if_supported "-XX:G1MixedGCLiveThresholdPercent=${G1_MIXED_GCLIVE_THRESHOLD_PERCENT:-65}"
    _append_if_supported "-XX:+UseStringDeduplication"
    _append_if_supported "-XX:+ParallelRefProcEnabled"
  elif [[ "${GC_MODE:-}" == "CMS" ]]; then
    echo "GC mode is CMS"
    _append_if_supported "-XX:+UseConcMarkSweepGC"
    _append_if_supported "-Xmn${XMN:-512m}"
    _append_if_supported "-XX:ParallelGCThreads=${PARALLEL_GC_THREADS:-2}"
    _append_if_supported "-XX:ConcGCThreads=${CONC_GC_THREADS:-1}"
    _append_if_supported "-XX:+UseCMSInitiatingOccupancyOnly"
    _append_if_supported "-XX:CMSInitiatingOccupancyFraction=${CMS_INITIATING_HEAP_OCCUPANCY_PERCENT:-92}"
    _append_if_supported "-XX:+CMSClassUnloadingEnabled"
    _append_if_supported "-XX:+CMSScavengeBeforeRemark"
    if [[ "${JAVA_MAJOR_VERSION}" -le "8" ]]; then
      _append_if_supported "-XX:+CMSIncrementalMode"
      _append_if_supported "-XX:CMSFullGCsBeforeCompaction=${CMS_FULL_GCS_BEFORE_COMPACTION:-5}"
      _append_if_supported "-XX:+ExplicitGCInvokesConcurrent"
      _append_if_supported "-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses"
    fi
  elif [[ -n "${GC_MODE:-}" ]]; then
    echo "Warning: Unknown GC mode '${GC_MODE}', using JVM defaults"
  else
    echo "No specific GC mode set, using JVM defaults"
  fi

  # GC log settings
  # GC 日志设置
  if [[ "${USE_GC_LOG:-N}" == "Y" ]]; then
    _append_if_supported "-XX:+PrintVMOptions"
    gc_log_path="${GC_LOG_PATH:-${LOG_HOME}}"

    # Create a GC log directory if it doesn't exist
    if [[ ! -d "${gc_log_path}" ]]; then
      mkdir -p "${gc_log_path}" || {
        echo "Warning: Failed to create GC log directory '${gc_log_path}', using default logs directory"
        gc_log_path="${LOG_HOME}"
      }
    fi

    # Ensure the directory is writable
    if [[ ! -w "${gc_log_path}" ]]; then
      echo "Warning: GC log directory '${gc_log_path}' is not writable, using default logs directory"
      gc_log_path="${LOG_HOME}"
      # Try to make the default directory writable if it exists
      if [[ -d "${gc_log_path}" && ! -w "${gc_log_path}" ]]; then
        chmod 755 "${gc_log_path}" 2>/dev/null || echo "Warning: Could not make '${gc_log_path}' writable"
      fi
    fi

    # Different GC log options for Java 9+ vs Java 8 and earlier
    if [[ "${JAVA_MAJOR_VERSION}" -gt "8" ]]; then
      # Java 9+ uses unified logging with -Xlog
      gc_log_file="${gc_log_path}/jvm_gc-%p-%t.log"
      echo "GC log path is '${gc_log_file}'."
      _append_if_supported "-Xlog:gc:file=${gc_log_file}:tags,uptime,time,level:filecount=${GC_LOG_FILE_COUNT:-10},filesize=${GC_LOG_FILE_SIZE:-100M}"
    else
      # Java 8 and earlier use multiple options
      gc_log_file="${gc_log_path}/jvm_gc.log"
      echo "GC log path is '${gc_log_file}'."
      # Touch the file to ensure it exists and check if it's writable
      touch "${gc_log_file}" 2>/dev/null || echo "Warning: Could not create GC log file '${gc_log_file}'"

      _append_if_supported "-Xloggc:${gc_log_file}"
      _append_if_supported "-verbose:gc"
      _append_if_supported "-XX:+PrintGCDetails"
      _append_if_supported "-XX:+PrintGCDateStamps"
      _append_if_supported "-XX:+PrintGCTimeStamps"
      _append_if_supported "-XX:+UseGCLogFileRotation"
      _append_if_supported "-XX:NumberOfGCLogFiles=${GC_LOG_FILE_COUNT:-10}"
      _append_if_supported "-XX:GCLogFileSize=${GC_LOG_FILE_SIZE:-100M}"
      _append_if_supported "-XX:+PrintGCCause"
      _append_if_supported "-XX:+PrintGCApplicationStoppedTime"
      _append_if_supported "-XX:+PrintTLAB"
      _append_if_supported "-XX:+PrintReferenceGC"
      _append_if_supported "-XX:+PrintHeapAtGC"
      _append_if_supported "-XX:+FlightRecorder"
      _append_if_supported "-XX:+PrintSafepointStatistics"
      _append_if_supported "-XX:PrintSafepointStatisticsCount=1"
      _append_if_supported "-XX:+DebugNonSafepoints"
      _append_if_supported "-XX:+SafepointTimeout"
      _append_if_supported "-XX:SafepointTimeoutDelay=500"
    fi
  else
    echo "Warning: GC log not enabled."
  fi

  # Heap dump settings
  # 堆转储设置
  if [[ "${USE_HEAP_DUMP:-N}" == "Y" ]]; then
    heap_dump_path="${HEAP_DUMP_PATH:-${LOG_HOME}}"

    # Create a heap dump directory if it doesn't exist
    if [[ ! -d "${heap_dump_path}" ]]; then
      mkdir -p "${heap_dump_path}" || {
        echo "Warning: Failed to create heap dump directory '${heap_dump_path}', using default logs directory"
        heap_dump_path="${LOG_HOME}"
      }
    fi

    echo "Heap dump path is '${heap_dump_path}/jvm_heap_dump.hprof'."
    _append_if_supported "-XX:HeapDumpPath=${heap_dump_path}/jvm_heap_dump.hprof"
    _append_if_supported "-XX:+HeapDumpOnOutOfMemoryError"
  else
    echo "Warning: JVM Heap Dump not enabled"
  fi

  # Large pages settings
  # 大页设置
  if [[ "${USE_LARGE_PAGES:-N}" == "Y" ]]; then
    echo "Use large pages."
    _append_if_supported "-XX:+UseLargePages"
  fi

  # JDWP debug settings
  # JDWP调试设置
  if [[ "${JDWP_DEBUG:-N}" == "Y" ]]; then
    echo "Attach to remote JVM using port ${JDWP_PORT:-5005}."
    _append_if_supported "-Xdebug"
    _append_if_supported "-Xrunjdwp:transport=dt_socket,address=${JDWP_PORT:-5005},server=y,suspend=n"
  fi

  # Process external configuration: if EXTERNAL_CONFIG_HOME directory exists and is not empty,
  # use it as an additional configuration location
  # 处理外部配置：如果 EXTERNAL_CONFIG_HOME 目录存在且非空，则将其作为额外的配置位置
  if [[ -d "${EXTERNAL_CONFIG_HOME}" ]]; then
    # Check if the directory is not empty without using ls
    if [[ -n "$(find "${EXTERNAL_CONFIG_HOME}" -mindepth 1 -maxdepth 1 2>/dev/null)" ]]; then
      echo "Using external configuration from ${EXTERNAL_CONFIG_HOME}"
      # springboot < 2.4
      JAVA_OPTS="${JAVA_OPTS} -Dspring.config.additional-location=optional:file:${EXTERNAL_CONFIG_HOME}/"
      # springboot >= 2.4
      #      JAVA_OPTS="${JAVA_OPTS} -Dspring.config.import=optional:file:${EXTERNAL_CONFIG_HOME}/"
    else
      echo "External configuration directory ${EXTERNAL_CONFIG_HOME} exists but is empty, skipping"
    fi
  fi

  # Set Spring Boot ports
  # 设置 Spring Boot 端口
  JAVA_OPTS="${JAVA_OPTS} -Dserver.port=${SERVER_PORT:-8888} -Dmanagement.server.port=${MANAGEMENT_SERVER_PORT:-9999}"

  # Add an option to ignore unrecognized VM options to prevent JVM startup failures
  #!! 不建议加这个参数(生产环境), 临时测试可用
  # JAVA_OPTS="-XX:+IgnoreUnrecognizedVMOptions ${JAVA_OPTS}"
}

# Initialize environment variables with defaults
JAVA_OPTS=${JAVA_OPTS:-}
APP_NAME=${APP_NAME:-application}
APP_HOME=${APP_HOME:-/workspace}
echo "APP_HOME is $APP_HOME"
DATA_HOME=${DATA_HOME:-/app/data}
echo "DATA_HOME is $DATA_HOME"
LOG_HOME=${LOG_HOME:-/app/logs}
echo "LOG_HOME is $LOG_HOME"
EXTERNAL_CONFIG_HOME=${EXTERNAL_CONFIG_HOME:-${APP_HOME}/config}
echo "EXTERNAL_CONFIG_HOME is $EXTERNAL_CONFIG_HOME"

# Create logs directory
# 创建日志目录
if [[ ! -d "${LOG_HOME}" ]]; then
  mkdir -p "${LOG_HOME}" || {
    echo "Error: failed to create logs directory $LOG_HOME"
    exit 1
  }
fi

# Main execution flow
_detect_java_environment || exit 1
_optimize_java_opts
if [[ -e "${APP_HOME}/jib-main-class-file" ]]; then
  echo "=> Running application built by jib-maven-plugin"
  # Use exec to replace the shell process with java, ensuring signals are properly handled
  exec java ${JAVA_OPTS} -cp $(cat "$APP_HOME"/jib-classpath-file) $(cat "$APP_HOME"/jib-main-class-file)
else
  echo "=> Running application built by spring-boot-maven-plugin"
  # Use exec to replace the shell process with java, ensuring signals are properly handled
  exec java ${JAVA_OPTS} -noverify -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.JarLauncher "$@"
fi
