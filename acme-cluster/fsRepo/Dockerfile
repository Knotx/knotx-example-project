FROM acme-core:latest

## Copy ACME specific configs
ADD conf ${KNOTX_HOME}/conf
ADD sample ${KNOTX_HOME}/sample

CMD ["run-knotx", "-cluster"]
