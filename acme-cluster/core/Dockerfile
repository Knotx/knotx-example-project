FROM knotx/knotx

## Add new dependencies to the Knot.x stack, a dependencies to your custom code
COPY knotx-stack.json ${KNOTX_HOME}/knotx-stack.json
RUN knotx resolve && rm -rf ${HOME}/.m2

## Copy ACME specific configs
COPY conf ${KNOTX_HOME}/conf

EXPOSE 8092

ENTRYPOINT ["knotx"]
CMD ["run-knotx", "-cluster"]
