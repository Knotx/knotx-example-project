
plugins {
    id("jacoco")
    id("maven-publish")
}


subprojects {
    group = "io.knotx"

    repositories {
        jcenter()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/groups/staging/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

apply(from = "gradle/javaAndUnitTests.gradle.kts")