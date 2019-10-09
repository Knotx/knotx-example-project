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

dependencies {
    subprojects.forEach { "dist"(project(":${it.name}")) }
}

sourceSets.named("test") {
    java.srcDir("functional/src/test/java")
}

allprojects {
    group = "io.knotx"

    repositories {
        jcenter()
        mavenLocal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/groups/staging/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

tasks.named("build") {
    dependsOn("runTest")
}

apply(from = "https://raw.githubusercontent.com/Knotx/knotx-starter-kit/master/gradle/docker.gradle.kts")
apply(from = "https://raw.githubusercontent.com/Knotx/knotx-starter-kit/master/gradle/javaAndUnitTests.gradle.kts")