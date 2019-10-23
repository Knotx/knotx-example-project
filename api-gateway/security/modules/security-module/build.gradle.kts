plugins {
    `java-library`
}

dependencies {
    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:${project.property("knotx.version")}"))
        implementation("$v-fragments-handler-api:${project.property("knotx.version")}")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-core")
        implementation("$v-rx-java2")
        implementation("$v-health-check")
        implementation("$v-auth-shiro")
        implementation("$v-auth-jwt")
        implementation("$v-auth-oauth2")
    }
    "org.apache".let { v ->
        compile("$v.httpcomponents:httpclient:4.5.3")
        compile("$v.commons:commons-lang3:3.9")
    }
}
