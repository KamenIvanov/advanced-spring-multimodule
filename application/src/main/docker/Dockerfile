FROM bellsoft/liberica-openjdk-alpine:25

ENV LOG_DIR=/runtime/logs
ENV JAR_LOCATION=/data/advanced-service.jar

WORKDIR /runtime

RUN mkdir -p "$LOG_DIR" \
    && chmod a+rwx "$LOG_DIR"

COPY scripts /scripts
COPY maven/application-${project.version}-exec.jar ${JAR_LOCATION}

EXPOSE 8080 8787

HEALTHCHECK --interval=5s --timeout=3s --retries=20 \
    CMD wget -q -O - -S http://127.0.0.1:8081/health || exit 1

ENTRYPOINT ["/bin/sh", "/scripts/run.sh"]
