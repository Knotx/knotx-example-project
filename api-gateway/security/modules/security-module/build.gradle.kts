plugins {
    `java-library`
}

dependencies {
    "io.knotx:knotx".let { v ->
        implementation(platform("$v-dependencies:${project.property("knotxVersion")}"))
        api("$v-server-http-api:${project.property("knotxVersion")}")
    }
    "io.vertx:vertx".let { v ->
        implementation("$v-core")
        implementation("$v-rx-java2")
        implementation("$v-health-check")
        implementation("$v-auth-shiro")
        implementation("$v-auth-jwt")
    }
    "org.apache".let { v ->
        compile("$v.httpcomponents:httpclient:4.5.3")
        compile("$v.commons:commons-lang3:3.9")
    }
}
