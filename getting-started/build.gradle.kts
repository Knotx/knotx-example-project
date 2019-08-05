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
    id("maven-publish")
    id("signing")
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

apply(from = "gradle/integrationTests.gradle.kts")
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

// -----------------------------------------------------------------------------
// Publication
// -----------------------------------------------------------------------------
tasks.register("publish-all") {
    dependsOn(gradle.includedBuilds.stream().map { ib -> ib.task(":publish") }.toArray())
    dependsOn(tasks.named("publish"))
}

tasks.register("publish-local-all") {
    dependsOn(gradle.includedBuilds.stream().map { ib -> ib.task(":publishToMavenLocal") }.toArray())
    dependsOn(tasks.named("publishToMavenLocal"))
}

publishing {
    publications {
        create<MavenPublication>("knotxDistribution") {
            artifactId = "knotx-stack"
            artifact(tasks.named("assembleDistribution").get())
            pom {
                name.set("Knot.x Stack")
                description.set("Distribution of Knot.x containing all dependencies, configurations and running scripts.")
                url.set("http://knotx.io")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("marcinczeczko")
                        name.set("Marcin Czeczko")
                        email.set("https://github.com/marcinczeczko")
                    }
                    developer {
                        id.set("skejven")
                        name.set("Maciej Laskowski")
                        email.set("https://github.com/Skejven")
                    }
                    developer {
                        id.set("tomaszmichalak")
                        name.set("Tomasz Michalak")
                        email.set("https://github.com/tomaszmichalak")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Knotx/knotx-stack.git")
                    developerConnection.set("scm:git:ssh://github.com:Knotx/knotx-stack.git")
                    url.set("http://knotx.io")
                }
            }
        }
        repositories {
            maven {
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                credentials {
                    username = if (project.hasProperty("ossrhUsername")) project.property("ossrhUsername")?.toString() else "UNKNOWN"
                    password = if (project.hasProperty("ossrhPassword")) project.property("ossrhPassword")?.toString() else "UNKNOWN"
                    println("Connecting with user: ${username}")
                }
            }
        }
    }
}
val subProjectPath = this.path
signing {
    setRequired({
        gradle.taskGraph.hasTask("$subProjectPath:publish") ||
                gradle.taskGraph.hasTask("$subProjectPath:publishMavenJavaPublicationToMavenRepository")
    })

    sign(publishing.publications["knotxDistribution"])
}
