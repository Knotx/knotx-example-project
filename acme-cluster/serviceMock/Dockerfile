FROM acme-core:latest

## Copy ACME specific configs
ADD conf ${KNOTX_HOME}/conf
COPY sample ${KNOTX_HOME}/sample

EXPOSE 3000

CMD ["run-knotx"]
