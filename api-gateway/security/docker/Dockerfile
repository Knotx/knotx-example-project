FROM knotx/knotx:$knotx_version
LABEL maintainer="Knot.x Project"

COPY ./out/knotx /usr/local/knotx

ENV JAVA_OPTS "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=18092"

CMD [ "knotx", "run-knotx" ]