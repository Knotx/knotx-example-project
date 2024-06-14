FROM knotx/knotx:$knotx_version
LABEL maintainer="Knot.x Project"

ENV JAVA_OPTS "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=18092"

COPY ./out/knotx /usr/local/knotx

HEALTHCHECK --interval=5s --timeout=2s --retries=12 \
  CMD curl --silent --fail localhost:8092/healthcheck || exit 1

CMD [ "knotx", "run-knotx" ]
