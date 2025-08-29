#!/usr/bin/env sh
# insert_archetype_version.sh
# Usage:
#   ./insert_archetype_version.sh -f archetype-catalog.xml -v 3.25.0 -r https://artifactory.example.com/artifactory/maven-public-virtual/
#   ./insert_archetype_version.sh -v 3.25.0 -r https://repo.example.com/maven/
#   ./insert_archetype_version.sh 3.25.0                        # 仍支持位置参数版本
#
# Options:
#   -f FILE     目标 archetype-catalog.xml（默认: ./archetype-catalog.xml）
#   -v VERSION  要插入的版本号（如 3.25.0），同时会插入 -SNAPSHOT
#   -r REPO     <repository> 的 URL（默认: ${REPO_URL:-https://artifactory.example.com/artifactory/maven-public-virtual/}）
#   -h          帮助

set -eu

CATALOG_FILE="${CATALOG_FILE:-archetype-catalog.xml}"
VERSION=""
REPO_URL_DEFAULT="https://artifactory.example.com/artifactory/maven-public-virtual/"
REPO_URL="${REPO_URL:-$REPO_URL_DEFAULT}"

usage() {
  echo "Usage: $0 [-f FILE] -v VERSION [-r REPO_URL]"
  echo "       $0 [-f FILE] VERSION"
  echo "Options:"
  echo "  -f FILE     path to archetype-catalog.xml (default: ./archetype-catalog.xml)"
  echo "  -v VERSION  e.g. 3.25.0 (also inserts 3.25.0-SNAPSHOT)"
  echo "  -r REPO_URL repository URL to write into <repository>"
  echo "  -h          help"
}

# --- parse args ---
while getopts ":f:v:r:h" opt; do
  case "$opt" in
  f) CATALOG_FILE="$OPTARG" ;;
  v) VERSION="$OPTARG" ;;
  r) REPO_URL="$OPTARG" ;;
  h) usage; exit 0 ;;
  :) echo "Option -$OPTARG requires an argument." >&2; usage; exit 1 ;;
  \?) echo "Unknown option: -$OPTARG" >&2; usage; exit 1 ;;
  esac
done
shift $((OPTIND-1))

# positional fallback for VERSION
if [ -z "${VERSION:-}" ] && [ $# -ge 1 ]; then
  VERSION="$1"
fi

[ -n "${VERSION:-}" ] || { echo "Missing VERSION. Use -v <version> (e.g., -v 3.25.0)." >&2; usage; exit 1; }
[ -f "$CATALOG_FILE" ] || { echo "File not found: $CATALOG_FILE" >&2; exit 1; }

# basic version validation
case "$VERSION" in
*[!0-9.]*|"") echo "Invalid version: $VERSION (digits and dots only)" >&2; exit 1 ;;
esac
# optional REPO_URL sanity check（宽松）
case "$REPO_URL" in
http://*|https://*) : ;;
*) echo "WARN: REPO_URL does not look like http(s) URL: $REPO_URL" >&2 ;;
esac

TMP="$(mktemp)"
cp "$CATALOG_FILE" "$CATALOG_FILE.bak"

awk -v ver="$VERSION" -v repo_url="$REPO_URL" '
function base(v){ sub(/-SNAPSHOT$/,"",v); return v }
# 返回纯数字零填充串方便字符串比较（不含点号，BSD awk 友好）
function norm(v,    a,i,max,out,x){
  split(v,a,".")
  max=(length(a)>5?length(a):5)
  out=""
  for(i=1;i<=max;i++){
    x=(i in a ? a[i]+0 : 0)
    out = out sprintf("%05d", x)
  }
  return out
}
function print_block(v,    i,g,indent,art,desc){
  g="space.x9x.radp"
  indent="\t\t"
  art[1]="scaffold-tiny";    desc[1]="极简版本(MVC架构)"
  art[2]="scaffold-simple";  desc[2]="轻量版(无模块,DDD架构)"
  art[3]="scaffold-lite";    desc[3]="轻量版(单机,DDD架构)"
  art[4]="scaffold-std";     desc[4]="标准版(分布式,DDD架构)"
  art[5]="scaffold-starter"; desc[5]="spring-boot-starter 脚手架"
  for(i=1;i<=5;i++){
    print ""
    print indent "<archetype>"
    print indent "\t<groupId>" g "</groupId>"
    print indent "\t<artifactId>" art[i] "</artifactId>"
    print indent "\t<version>" v "</version>"
    print indent "\t<repository>" repo_url "</repository>"
    print indent "\t<description>" desc[i] "</description>"
    print indent "</archetype>"
  }
}

BEGIN{
  ver_base = base(ver)
  need_rel = 1
  need_snap = 1
  found_insert = 0
  N=0
}
{
  lines[++N]=$0
  if ($0 ~ /<\/archetypes>/) end_idx=N

  # BSD-safe：字符串正则 + RSTART/RLENGTH
  ver_re = "<version>[^<][^<]*</version>"
  if (match($0, ver_re)) {
    v = substr($0, RSTART, RLENGTH)
    gsub(/^<version>|<\/version>$/, "", v)

    b = v; sub(/-SNAPSHOT$/, "", b)

    if (v == ver)             need_rel=0
    if (v == ver "-SNAPSHOT") need_snap=0

    if (!found_insert && norm(b) > norm(ver_base)) {
      q=NR
      while (q>1 && index(lines[q], "<archetype>") == 0) q--
      if (q<1) q=1
      insert_idx=q
      found_insert=1
    }
  }
}
END{
  if (!need_rel && !need_snap) {
    for(i=1;i<=N;i++) print lines[i]
    exit 0
  }
  if (!found_insert) insert_idx = (end_idx ? end_idx : N+1)

  for(i=1;i<insert_idx;i++) print lines[i]
  if (need_rel)  print_block(ver)
  if (need_snap) print_block(ver "-SNAPSHOT")
  for(i=insert_idx;i<=N;i++) print lines[i]
}
' "$CATALOG_FILE" > "$TMP"

mv "$TMP" "$CATALOG_FILE"
echo "Done. Updated $CATALOG_FILE (backup at $CATALOG_FILE.bak)"
