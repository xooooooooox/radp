#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")" && pwd)"
source ${SCRIPT_DIR}/catalina.sh start
exit
