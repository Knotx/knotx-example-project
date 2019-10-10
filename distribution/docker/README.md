[![Build Status](https://dev.azure.com/knotx/Knotx/_apis/build/status/Knotx.knotx-starter-kit?branchName=master)](https://dev.azure.com/knotx/Knotx/_build/latest?definitionId=3&branchName=master)

# Knot.x Starter Kit
Starter Kit is a template project that simplifies Knot.x project setup. It allows you to 
customize the [Knot.x distribution](https://github.com/Knotx/knotx-stack) with your own modules and
configuration entries. 

Starter Kit artifacts (see the build/distributions folder) are: 
- a ZIP file when `./gradlew build-stack` or `./gradlew build`
- a custom Docker image when `./gradlew build-docker`

## How to build & run
To start a new Knot.x project simply download the chosen ZIP version from https://github.com/Knotx/knotx-starter-kit/tags
or use the development one (from the `master` branch). Follow [Development process](https://github.com/Knotx/knotx-aggregator) 
instructions when use the development version.

Then unzip Starter Kit and run:

### Build ZIP distribution
```
$> ./gradlew build-stack
```
to:
- build all your custom Knot.x modules
- download Knot.x Stack artifact, add all required dependencies (including your custom modules and 
its transitive dependencies), override configs with `/knotx/config`
- build the custom ZIP artifact

Then, go to the `build/distributions` directory, unzip the ZIP artifact, start Knot.x:
```
chmod +x bin/knotx
bin/knotx run-knotx
```

and validate urls:
- [localhost:8092/api/v1/example](http://localhost:8092/api/v1/example)
- [localhost:8092/api/v2/example](http://localhost:8092/api/v2/example)

### Build & validate Docker image
```
$> ./gradlew build-docker
```

to:
- build all your custom Knot.x modules
- prepare your custom Docker image with all required dependencies (including your custom modules and 
its transitive dependencies) and configs
- **validate your Docker image with system tests**

Then, start Docker container:
```
docker run -p8092:8092 knotx/knotx-starter-kit
```

and validate urls:
- [localhost:8092/api/v1/example](http://localhost:8092/api/v1/example)
- [localhost:8092/api/v2/example](http://localhost:8092/api/v2/example)

## What does it contain

### Custom modules

- [API Handler](https://github.com/Knotx/knotx-starter-kit/tree/master/modules/example-api) 
that contains example implementation of a [Handler](https://github.com/Knotx/knotx-server-http/tree/master/api#routing-handlers) 
- [Healthcheck](https://github.com/Knotx/knotx-starter-kit/tree/master/modules/health-check)
that holds example implementation of a [Vert.x Healthcheck](https://vertx.io/docs/vertx-health-check/java/)
- [Action](https://github.com/Knotx/knotx-starter-kit/tree/master/modules/example-action) that
contains example implementation of a [Knot.x Action](https://github.com/Knotx/knotx-fragments/tree/master/handler/api#action)


All modules are built into JAR files and copied to the `KNOTX_HOME/lib` folder in the project Docker image. 
Also all transitive dependencies are automatically downloaded and added to the Docker image.

### Configuration

You can also override the [default Knot.x configuration](https://github.com/Knotx/knotx-stack/tree/master/src/main/packaging/conf)
with your custom settings. All files from the `/conf` directory are copied to `KNOTX_HOME/conf`. So you
can easily modify the Knot.x configuration, reconfigure the logger or update an [Open API](https://github.com/OAI/OpenAPI-Specification) specification.

If you want to add a dependency that is not connected with any custom module such as 
[Knot.x Dashboard](https://github.com/Knotx/knotx-dashboard) you can add this entry in 
`/build.gradle.kts`:

```
dependencies {
    subprojects.forEach { "dist"(project(":${it.name}")) }
    "dist"("io.knotx:knotx-dashboard:${project.version}")
}
```

### Dockerfile

[Dockerfile](https://github.com/Knotx/knotx-starter-kit/blob/master/docker/Dockerfile) is defined 
in the `docker` folder and extends the [Base Knot.x Docker image](https://hub.docker.com/r/knotx/knotx).

## How to debug

### ZIP distribution
Simply uncomment `# JVM_DEBUG` line in the `bin/knotx` starting script.

### Docker debugging
Edit `Dockerfile` in `docker` folder by adding
```dockerfile
RUN sed -i 's/# JVM_DEBUG=/JVM_DEBUG=/g' /usr/local/knotx/bin/knotx
```
Start Docker container with additional port
```cmd
docker run -p8092:8092 -p18092:18092 knotx/knotx-starter-kit
```

#### Startup debugging - use this when debugging `start()` methods
In addition to above edit `Dockerfile` by adding
```dockerfile
RUN sed -i 's/suspend=n/suspend=y/g' /usr/local/knotx/bin/knotx
```

Comment out health-check section from `Dockerfile`
```dockerfile
#HEALTHCHECK --interval=5s --timeout=2s --retries=12 \
#  CMD curl --silent --fail localhost:8092/healthcheck || exit 1
```

**IMPORTANT !** - Make sure that `CMD [ "knotx", "run-knotx" ]` is a last command in `Dockerfile`