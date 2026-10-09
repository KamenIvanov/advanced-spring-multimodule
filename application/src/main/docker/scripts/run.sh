#!/bin/sh

DEBUG_PARAM=""
if [ "true" == "$DEBUG" ]; then
	DEBUG_PARAM="-Xrunjdwp:server=y,transport=dt_socket,address=0.0.0.0:8787,suspend=n"
fi

for var in DB_URL DB_USER DB_PASSWORD ACTIVE_PROFILE; do
    eval "value=\$$var"
    if [ -z "$value" ]; then
        echo "$(date +"%T"): FATAL: $var is not set"
        exit 1
    fi
done

echo "$(date +"%T"): =======Multimodule Environment Variables========"
echo "$(date +"%T"): ACTIVE_PROFILE=$ACTIVE_PROFILE"
echo "$(date +"%T"): DB_URL=$DB_URL"
echo "$(date +"%T"): DB_USER=$DB_USER"
echo "$(date +"%T"): PORT=$HTTP_PORT"
echo "$(date +"%T"): ===================================="

# We set the debug=false because Spring will read this variable and if it's true, it will set all Spring, Tomcat, Jetty and Hibernate
# loggers to DEBUG and TRACE no matter of logback.xml configuration. If you want to enable them, just set it to true

exec java $JAVA_OPTS \
    -Dspring.jmx.enabled=false \
	  -Dhttp.server.port=$HTTP_PORT \
    -Dspring.profiles.active=$ACTIVE_PROFILE \
    -Dspring.datasource.url=$DB_URL \
    -Dspring.datasource.username=$DB_USER \
    -Dspring.datasource.password=$DB_PASSWORD \
    -Ddebug=false \
    -jar $JAR_LOCATION
