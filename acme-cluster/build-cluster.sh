#
# Copyright (C) 2018 Knot.x Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License
#
#!/usr/bin/env bash
DOCKER_IMAGE_VERSION="${1:-latest}"

docker build -t acme/acme-core:${DOCKER_IMAGE_VERSION} core/
docker build -t acme/acme-data-bridge-adapter:${DOCKER_IMAGE_VERSION} dataSourceHttp/
docker build -t acme/acme-action-adapter:${DOCKER_IMAGE_VERSION} formsAdapter/
docker build -t acme/acme-fs-repo:${DOCKER_IMAGE_VERSION} fsRepo/
docker build -t acme/acme-gateway:${DOCKER_IMAGE_VERSION} gateway/
docker build -t acme/acme-te:${DOCKER_IMAGE_VERSION} templateEngine/
docker build -t acme/acme-http-repo:${DOCKER_IMAGE_VERSION} acmeRepo/
docker build -t acme/acme-services:${DOCKER_IMAGE_VERSION} acmeServices/
