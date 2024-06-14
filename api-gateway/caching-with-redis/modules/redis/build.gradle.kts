plugins {
    `java-library`
}

dependencies {
    implementation(group = "org.apache.commons", name = "commons-lang3")

    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:${project.property("knotxVersion")}"))
        implementation("$v-server-http-api:${project.property("knotxVersion")}")
        implementation("$v-fragments-action-core:${project.property("knotxVersion")}")
//        implementation("$v-fragments-handler-core:${project.property("knotxVersion")}")
        implementation("$v-server-http-common-placeholders:${project.property("knotxVersion")}")
        implementation("$v-commons:${project.property("knotxVersion")}")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-web")
        implementation("$v-web-client")
        implementation("$v-rx-java2")
        implementation("$v-circuit-breaker")
        implementation("$v-redis-client")
    }
}
