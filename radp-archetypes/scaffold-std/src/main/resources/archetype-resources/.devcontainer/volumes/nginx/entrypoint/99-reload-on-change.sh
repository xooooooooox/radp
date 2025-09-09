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
  # Portable signature without relying on GNU find -printf (works on busybox/alpine).
  # 1) List files under the watched paths (if any), sorted for stable order.
  # 2) Hash each file's content with its filename, then hash the full list to one digest.
  # shellcheck disable=SC2086
  FILES=$(find $WATCH_PATHS -type f 2>/dev/null | sort)
  if [ -n "$FILES" ]; then
    # Note: filenames are appended to ensure file renames/creates/deletes affect the signature.
    # shellcheck disable=SC2086
    (
      for f in $FILES; do
        # Combine file path and its content hash for better sensitivity to file set changes.
        if HASH=$(sha256sum "$f" 2>/dev/null); then
          echo "$HASH $f"
        else
          # If a file disappears mid-scan, still affect the signature deterministically.
          echo "missing $f"
        fi
      done
    ) | sha256sum 2>/dev/null | awk '{print $1}'
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
