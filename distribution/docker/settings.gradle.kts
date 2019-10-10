/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
rootProject.name = "distribution-docker"

include("hellohandler")
include("example-api")
include("health-check")
include("example-action")

project(":hellohandler").projectDir = file("modules/hellohandler")
project(":example-api").projectDir = file("modules/example-api")
project(":health-check").projectDir = file("modules/health-check")
project(":example-action").projectDir = file("modules/example-action")
