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
import org.nosphere.apache.rat.RatTask

plugins {
    id("io.knotx.java-library") version "0.1.1"
    id("io.knotx.unit-test") version "0.1.1"
    id("org.nosphere.apache.rat") version "0.4.0"
    id("idea")
}

project.group = "io.knotx"

// we do not use mavenLocal - instead please setup composite build environment (https://github.com/Knotx/knotx-aggregator)
repositories {
    jcenter()
    mavenLocal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/staging/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

apply(from = "gradle/distribution.gradle.kts")

// -----------------------------------------------------------------------------
// License headers validation
// -----------------------------------------------------------------------------
tasks {
    named<RatTask>("rat") {
        excludes.addAll("*.md", "**/*.md", "**/bin/*", ".travis.yml", "**/build/*", "**/out/*", "**/*.json", "**/*.conf", "**/*.html", "**/*.properties", ".idea", ".composite-enabled")
    }
    getByName("build").dependsOn("rat")
}
