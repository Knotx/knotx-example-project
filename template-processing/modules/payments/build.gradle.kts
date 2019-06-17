val knotxVersion = "2.0.0-SNAPSHOT"

plugins {
    `java-library`
}

dependencies {
    implementation(group = "org.apache.commons", name = "commons-lang3")

    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:$knotxVersion"))
        implementation("$v-server-http-api:$knotxVersion")
        implementation("$v-fragments-handler-api:$knotxVersion")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-web")
        implementation("$v-web-client")
        implementation("$v-rx-java2")
        implementation("$v-circuit-breaker")
    }
}
