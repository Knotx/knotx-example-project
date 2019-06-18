val knotxVersion = "2.0-RC3"

plugins {
    `java-library`
}

dependencies {
    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:$knotxVersion"))
        implementation("$v-server-http-api:$knotxVersion")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-web")
        implementation("$v-web-client")
        implementation("$v-rx-java2")
        implementation("$v-health-check")
    }
}
