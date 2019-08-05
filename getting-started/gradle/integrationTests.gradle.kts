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

dependencies {
    "testImplementation"(platform("io.knotx:knotx-dependencies:${project.version}"))

    "testImplementation"(group = "io.vertx", name = "vertx-core")
    "testImplementation"(group = "io.vertx", name = "vertx-service-proxy")
    "testImplementation"(group = "io.vertx", name = "vertx-rx-java2")
    "testImplementation"(group = "io.vertx", name = "vertx-codegen")
    "testImplementation"(group = "io.vertx", name = "vertx-junit5")
    "testImplementation"(group = "io.vertx", name = "vertx-unit")
    "testImplementation"(group = "com.github.tomakehurst", name = "wiremock")

    "testImplementation"("io.knotx:knotx-commons:${project.version}")
    "testImplementation"("io.knotx:knotx-launcher:${project.version}")
    "testImplementation"("io.knotx:knotx-junit5:${project.version}")
    "testImplementation"("io.knotx:knotx-fragments-api:${project.version}")
    "testImplementation"("io.knotx:knotx-server-http-core:${project.version}")
    "testImplementation"("io.knotx:knotx-fragments-supplier-html-splitter:${project.version}")
    "testImplementation"("io.knotx:knotx-fragments-supplier-single-fragment:${project.version}")
    "testImplementation"("io.knotx:knotx-fragments-assembler:${project.version}")
    "testImplementation"("io.knotx:knotx-repository-connector-fs:${project.version}")
    "testImplementation"("io.knotx:knotx-repository-connector-http:${project.version}")
    "testImplementation"("io.knotx:knotx-fragments-handler-core:${project.version}")
    "testImplementation"("io.knotx:knotx-action-http:${project.version}")
    "testImplementation"("io.knotx:knotx-template-engine-core:${project.version}")
    "testImplementation"("io.knotx:knotx-template-engine-handlebars:${project.version}")
}