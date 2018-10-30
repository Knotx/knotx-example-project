FROM acme-core:latest

## Copy ACME specific configs
COPY conf ${KNOTX_HOME}/conf
COPY sample ${KNOTX_HOME}/sample

CMD ["run-knotx", "-cluster"]
