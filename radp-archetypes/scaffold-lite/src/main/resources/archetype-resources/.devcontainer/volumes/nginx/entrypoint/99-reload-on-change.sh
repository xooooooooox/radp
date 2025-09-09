#!/bin/sh
# Auto-reload Nginx when configuration files change.
# This script is executed by the official Nginx image via /docker-entrypoint.d
# It starts a background watcher and returns immediately so Nginx can start normally.

set -eu

WATCH_INTERVAL="${NGINX_WATCH_INTERVAL:-2}"
# Monitor the main config file and the conf.d directory
WATCH_PATHS=${WATCH_PATHS:-"/etc/nginx/nginx.conf /etc/nginx/conf.d"}

log() {
  echo "[nginx-watch] $*"
}

calc_signature() {
  # Use file path and modification time to detect changes; robust and cheap.
  # If directory is empty, find prints nothing; we handle empty case by echoing a placeholder.
  # shellcheck disable=SC2086
  if SIG=$(find $WATCH_PATHS -type f -printf '%p %T@\n' 2>/dev/null | sort | sha256sum 2>/dev/null); then
    printf '%s' "${SIG%% *}"
  else
    printf '%s' "none"
  fi
}

(
  last_sig=""
  # Small initial delay to avoid racing with first-time config generation/copies
  sleep 0.5 || true
  while :; do
    cur_sig="$(calc_signature)"
    if [ "$cur_sig" != "$last_sig" ]; then
      if [ -n "$last_sig" ]; then
        # Only attempt reload after the initial snapshot
        if nginx -t; then
          log "Change detected; reloading Nginx"
          nginx -s reload || log "Reload command failed"
        else
          log "Invalid configuration; skip reload"
        fi
      else
        log "Initial configuration snapshot taken"
      fi
      last_sig="$cur_sig"
    fi
    sleep "$WATCH_INTERVAL" || sleep 2
  done
) &

# Script ends here; background watcher will keep running.
