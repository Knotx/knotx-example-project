plugins {
    id("io.knotx.java-library")
}

dependencies {
    implementation(enforcedPlatform("com.graphql-java:graphql-java:6.0"))

    implementation(group = "org.apache.commons", name = "commons-lang3")

    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:${project.property("knotxVersion")}"))
        implementation("$v-server-http-api:${project.property("knotxVersion")}")
        implementation("$v-fragments-action-api:${project.property("knotxVersion")}")
        implementation("$v-fragments-action-library:${project.property("knotxVersion")}")
        implementation("$v-fragments-task-api:${project.property("knotxVersion")}")
        implementation("$v-fragments-task-engine:${project.property("knotxVersion")}")
        implementation("$v-fragments-task-factory-default:${project.property("knotxVersion")}")

        testImplementation("$v-junit5:${project.property("knotxVersion")}")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-web-graphql")
        implementation("$v-web")
        implementation("$v-web-client")
        implementation("$v-rx-java2")
        implementation("$v-circuit-breaker")

        testImplementation("$v-unit")
        testImplementation("$v-junit5")
    }

    testImplementation(group = "org.mockito", name = "mockito-core", version = "3.4.4")
    testImplementation(group = "org.mockito", name = "mockito-junit-jupiter", version = "3.4.4")
}

sourceSets {
    test {
        resources.srcDir("../../knotx/conf")
    }
}

tasks.withType<Test>().configureEach {
    systemProperties(Pair("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory"))

    failFast = true
    useJUnitPlatform()
    testLogging {
        events = setOf(org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED)
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }
}
