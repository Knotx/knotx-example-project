FROM knotx/knotx

## Copy ACME specific configs
ADD acme-stack/src/main/packaging/conf ${KNOTX_HOME}/conf

## Add new dependencies to the Knot.x stack, a dependencies to your custom code
COPY acme-stack/src/main/descriptor/knotx-stack.json ${KNOTX_HOME}/knotx-stack.json
RUN knotx resolve && rm -rf ${HOME}/.m2

EXPOSE 8092

ENTRYPOINT ["knotx"]
CMD ["run-knotx"]