#!/usr/bin/env bash

../gradlew clean build
docker build . -t template-processing/knotx
docker service update --force workshop2_knotx
docker service logs -f workshop2_knotx
