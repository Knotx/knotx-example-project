[![][travis img]][travis]
[![][license img]][license]

# Knot.x example project
The example project shows how you can use Knot.x and develop it in a real project. Uses the 
[Knot.x Stack](https://github.com/Knotx/knotx-stack) distribution and provides custom configuration 
files, sample content and modules. 

## Prerequisites
- JDK 8+
- Maven or Gradle

## Getting started
The example project consists of:

- `acme-stack` - contains all configuration files and the sample content
- `acme-action-adapter-http` - handles forms submit requests (POSTs), executes business logic and redirects to the next step / confirmation page / stay on the same page
- `acme-gateway` - provides the Gateway API interface that can be used by front-end integration
- `acme-handlebars-ext` - adds custom Handlebars directives
- `acme-sample-service` - runs the HTTP server on the `3000` port, which mocks real JSON service responses

The `acme-stack` module contains all required artifacts to build the example distribution. The result 
of the build process is a ZIP file that you can deploy to the server, extract and run with 
`bin\knotx run-knotx`. The `run-knotx` command starts all Knot.x modules, configures logs, and sets 
Java options. Note that all dependencies like `acme-action-adapter-http`, `acme-gateway`, 
`acme-handlebars-ext` and `acme-sample-service` are build and copied to the distribution.

### How to build
Clone the repository and build the example project:

#### Maven build

Let's run the command below:
```
$> mvn package
```
The distribution ZIP file location is `acme-stack/target/knotx-example-project-stack-X.X.X.zip`.

#### Gradle build
Let's run the command below:
```
$> gradlew
```
The distribution ZIP file location is `acme-stack/build/distributions/knotx-example-project-stack-X.X.X.zip`

### How to run
- Download and unpack latest [knotx-example-project-stack](https://bintray.com/knotx/downloads/examples)
or
- clone the repository and build the example project

#### Run locally:
Go to the `acme-stack` folder in the unpacked zip file and run the Knot.x script 

##### Unix and MacOS
```
$> bin/knotx run-knotx
```

##### Windows
```
$> bin\knotx.bat run-knotx
```

#### Run docker
Build image from dockerfile being in the `knotx-example-project` folder
```
$> docker build -t acme/knotx-example .
```
Run Knot.x container in background
```
$> docker run -d -p8092:8092 acme/knotx-example knotx-example
```

Follow logs
```
$> docker logs -f knotx-example
```

#### Run Knot.x cluster
Clone this repository and go to `acme-cluster` folder and run the Knot.x cluster
```
$> docker-compose up
```

### How to verify

Knot.x works in two modes:
  - templating engine with custom business logic that integrates with any data source using 
  [Knot.x Data Bridge](https://github.com/Knotx/knotx-data-bridge) and 
  [Handlebars](https://github.com/Cognifide/knotx/wiki/HandlebarsKnot) (back-end integration)
    - [http://localhost:8092/content/simple.html](http://localhost:8092/content/simple.html)
    - [http://localhost:8092/content/login/step1.html](http://localhost:8092/content/login/step1.html)
  - [Gateway mode](https://github.com/Cognifide/knotx/wiki/GatewayMode) providing REST API (front-end integration)
    - [http://localhost:8092/customFlow/](http://localhost:8092/customFlow/)

[travis]:https://travis-ci.org/Knotx/knotx-example-project
[travis img]:https://travis-ci.org/Knotx/knotx-example-project.svg?branch=master

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg
