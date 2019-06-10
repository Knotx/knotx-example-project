plugins {
    `java-library`
}


dependencies {
    implementation(platform("io.knotx:knotx-dependencies:${project.version}"))
    implementation("io.knotx:knotx-server-http-api:${project.version}")
    implementation("io.knotx:knotx-fragment-api:${project.version}")
    implementation("io.knotx:knotx-fragments-handler-api:${project.version}")
    implementation(group = "org.apache.commons", name = "commons-lang3")
    implementation(group = "io.vertx", name = "vertx-web")
    implementation(group = "io.vertx", name = "vertx-web-client")
    implementation(group = "io.vertx", name = "vertx-rx-java2")
    implementation(group = "io.vertx", name = "vertx-circuit-breaker")
}
