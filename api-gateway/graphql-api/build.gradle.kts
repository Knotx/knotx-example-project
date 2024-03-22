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

repositories {
    jcenter()
    gradlePluginPortal()
}

dependencies {
    subprojects.forEach { "dist"(project(":${it.name}")) }

    testImplementation(group = "io.vertx", name = "vertx-core")
    testImplementation(group = "io.rest-assured", name = "rest-assured", version = "3.3.0")
    testImplementation(group = "com.graphql-java", name = "graphql-java", version = "6.0")
}

sourceSets.named("test") {
    java.srcDir("functional/src/test/java")
}

allprojects {
    group = "io.knotx"

    repositories {
        jcenter()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}

tasks.named("build") {
    dependsOn("build-stack")
}

tasks.register("build-docker") {
    group = "docker"
    dependsOn("runFunctionalTest")
}

tasks.register("build-stack") {
    group = "stack"
    // https://github.com/Knotx/knotx-gradle-plugins/blob/master/src/main/kotlin/io/knotx/distribution.gradle.kts
    dependsOn("assembleCustomDistribution")
    mustRunAfter("build-docker")
}

apply(from = "https://raw.githubusercontent.com/Knotx/knotx-starter-kit/${project.property("knotxVersion")}/gradle/docker.gradle.kts")
apply(from = "https://raw.githubusercontent.com/Knotx/knotx-starter-kit/${project.property("knotxVersion")}/gradle/javaAndUnitTests.gradle.kts")