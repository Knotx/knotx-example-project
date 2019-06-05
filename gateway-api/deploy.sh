#!/usr/bin/env bash

../gradlew clean build
docker build . -t gateway-api/knotx
docker service update --force gateway-api_knotx
docker service logs -f gateway-api_knotx
