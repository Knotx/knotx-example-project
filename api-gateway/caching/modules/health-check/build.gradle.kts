plugins {
    `java-library`
}

dependencies {
    implementation(platform("io.knotx:knotx-dependencies:${project.property("knotxVersion")}"))
    "io.knotx:knotx".let { v ->
        implementation("$v-server-http-api:${project.property("knotxVersion")}")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-core")
        implementation("$v-rx-java2")
        implementation("$v-health-check")
    }

}
