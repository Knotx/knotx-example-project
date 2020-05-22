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
plugins {
    id("io.knotx.distribution")
    id("com.bmuschko.docker-remote-api")
    id("java")
}

configurations {
    register("wiremockExtensions")
}

dependencies {
    subprojects.forEach { "dist"(project(":${it.name}")) }
    "wiremockExtensions"("com.opentable:wiremock-body-transformer:1.1.3") { isTransitive = false }
}

sourceSets.named("test") {
    java.srcDir("functional/src/test/java")
}

allprojects {
    group = "io.knotx"

    repositories {
        jcenter()
        gradlePluginPortal()
    }
}

val downloadWireMockExtensions = tasks.register<Copy>("downloadWiremockExtensions") {
    from(configurations.named("wiremockExtensions"))
    into("../../common-services/webapi/extensions")
}

tasks.named("build") {
    dependsOn(downloadWireMockExtensions, "runFunctionalTest")
}

apply(from = "https://raw.githubusercontent.com/Knotx/knotx-starter-kit/${project.property("knotxVersion")}/gradle/docker.gradle.kts")
apply(from = "https://raw.githubusercontent.com/Knotx/knotx-starter-kit/${project.property("knotxVersion")}/gradle/javaAndUnitTests.gradle.kts")