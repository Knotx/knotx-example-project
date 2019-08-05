/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

val downloadDir = file("${buildDir}/download")
val distributionDir = file("${buildDir}/out")
val stackName = "knotx"
val stackDistribution = "knotx-stack-${version}.zip"

configurations {
    register("dist")
}

dependencies {
    "dist"("io.knotx:knotx-launcher:${project.version}")
    "dist"("io.knotx:knotx-server-http-core:${project.version}")
    "dist"("io.knotx:knotx-repository-connector-fs:${project.version}")
    "dist"("io.knotx:knotx-repository-connector-http:${project.version}")
    "dist"("io.knotx:knotx-fragments-supplier-html-splitter:${project.version}")
    "dist"("io.knotx:knotx-fragments-supplier-single-fragment:${project.version}")
    "dist"("io.knotx:knotx-fragments-assembler:${project.version}")
    "dist"("io.knotx:knotx-fragments-handler-core:${project.version}")
    "dist"("io.knotx:knotx-action-http:${project.version}")
    "dist"("io.knotx:knotx-template-engine-core:${project.version}")
    "dist"("io.knotx:knotx-template-engine-handlebars:${project.version}")
    "dist"("io.netty:netty-tcnative-boringssl-static:2.0.17.Final")
}

val cleanDistribution = tasks.register<Delete>("cleanDistribution") {
    delete(listOf(distributionDir))
}

val copyConfigs = tasks.register<Copy>("copyConfigs") {
    from(file("src/main/packaging"))
    into(file("${distributionDir}/${stackName}"))
}

val downloadDeps = tasks.register<Copy>("downloadDeps") {
    from(configurations.named("dist"))
    into("${distributionDir}/${stackName}/lib")
}

val assembleDistribution = tasks.register<Zip>("assembleDistribution") {
    archiveName = stackDistribution
    from(distributionDir)
}

assembleDistribution {
    dependsOn(copyConfigs, downloadDeps)
}

tasks.named("build") { finalizedBy(assembleDistribution) }
tasks.named("clean") { dependsOn(cleanDistribution) }