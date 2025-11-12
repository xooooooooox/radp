#!/bin/sh
# Usage: ./update_archetype_default_values.sh 2.26.2 2.27

from=${1:?"Please specify previous radp version"}
to=${2:?"Please specify target radp version"}

SCRIPT_DIR=$(dirname "$(realpath "$0")")
PROJECT_ROOT=${SCRIPT_DIR}/../..

# change archetype.properties radpVersion=$version
find "$PROJECT_ROOT/radp-archetypes" -type f -name 'archetype.properties' -exec \
  sed -i '' -E '/^[[:space:]]*#/! s/^([[:space:]]*radpVersion=).*/\1'"$to"'/' {} +

# change archetype-metadata.xml <defaultValue>$from</defaultValue> -> <defaultValue>$to</defaultValue>
find "$PROJECT_ROOT/radp-archetypes" -type f -name 'archetype-metadata.xml' -exec \
  sed -i '' -E 's#(<defaultValue)>'"$from"'(</defaultValue>)#\1>'"$to"'\2#g' {} +
