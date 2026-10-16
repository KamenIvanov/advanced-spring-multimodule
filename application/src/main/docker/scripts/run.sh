#!/bin/sh
set -e

for var in DB_URL DB_USER DB_PASSWORD ACTIVE_PROFILE; do
    eval "value=\$$var"
    if [ -z "$value" ]; then
        echo "$(date +"%T"): FATAL: $var is not set"
        exit 1
    fi
done

DEBUG_OPTS=""
if [ "${DEBUG:-false}" = "true" ]; then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787"
fi

# Strip carriage returns: .env files edited on Windows carry them,
# and a trailing \r makes the JVM reject the last option.
JAVA_OPTS=$(printf '%s' "${JAVA_OPTS:-}" | tr -d '\r')

echo "$(date +"%T"): ===== Environment ====="
echo "$(date +"%T"): ACTIVE_PROFILE=$ACTIVE_PROFILE"
echo "$(date +"%T"): DB_URL=$DB_URL"
echo "$(date +"%T"): DB_USER=$DB_USER"
echo "$(date +"%T"): ======================="

cd /runtime

# $DEBUG_OPTS and $JAVA_OPTS are deliberately unquoted: word splitting is how
# whitespace-separated JVM options arrive at java as separate arguments.
# Options whose values contain spaces belong in JAVA_TOOL_OPTIONS instead.
# exec allows Java to receive container shutdown signals directly.
exec java $DEBUG_OPTS $JAVA_OPTS \
    "-Dspring.profiles.active=$ACTIVE_PROFILE" \
    "-Dspring.datasource.url=$DB_URL" \
    "-Dspring.datasource.username=$DB_USER" \
    "-Dspring.datasource.password=$DB_PASSWORD" \
    -Ddebug=false \
    -jar "${JAR_LOCATION:?JAR_LOCATION must be set}"
