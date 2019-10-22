plugins {
    `java-library`
}

dependencies {
    implementation(enforcedPlatform("com.graphql-java:graphql-java:6.0"))

    implementation(group = "org.apache.commons", name = "commons-lang3")

    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:${project.property("knotx.version")}"))
        implementation("$v-server-http-api:${project.property("knotx.version")}")
        implementation("$v-fragments-engine:${project.property("knotx.version")}")
        implementation("$v-fragments-api:${project.property("knotx.version")}")
        implementation("$v-fragments-handler-api:${project.property("knotx.version")}")
        implementation("$v-fragments-handler-core:${project.property("knotx.version")}")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-web-graphql")
        implementation("$v-web")
        implementation("$v-web-client")
        implementation("$v-rx-java2")
        implementation("$v-circuit-breaker")
    }
}
