#!/usr/bin/env bash

payments-action/gradlew -p payments-action clean build
cp -r payments-action/build/libs/*.jar knotx/lib/
docker service update --force workshop2_knotx
docker service logs -f workshop2_knotx
