FROM bellsoft/liberica-openjdk-alpine:25

ENV LOGS_FOLDER /runtime/logs
ENV HTTP_PORT 8080
ENV JAR_LOCATION /data/multimodule-service.jar

RUN mkdir -p $LOGS_FOLDER && chmod a+rwx $LOGS_FOLDER

COPY maven/*-exec.jar ${JAR_LOCATION}
COPY scripts /scripts

RUN chmod a+x /scripts/*.sh \
    && mkdir -p /runtime/logs \
    && chmod a+rw /runtime/logs

# Expose Java debug port
EXPOSE 8787

HEALTHCHECK --interval=5s --timeout=3s --retries=20 \
    CMD wget -q -O - -S http://0.0.0.0:8081/health || exit 1

ENTRYPOINT [ "/bin/sh", "/scripts/run.sh" ]
