#!/bin/bash
set -e

if ! command -v gitlab-runner >/dev/null 2>&1; then
  curl -L "https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh" | sudo bash
  sudo apt install gitlab-runner -y
  gitlab-runner status || {
    echo "Failed to install gitlab runner"
    exit 1
  }
fi
