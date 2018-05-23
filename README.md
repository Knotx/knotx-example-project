[![][travis img]][travis]
[![][license img]][license]

# Running the Knot.x example project

## Prerequisites
- JDK 8+

## Getting started
### Run locally
- Download and unpack latest [knotx-example-project-stack](https://bintray.com/knotx/downloads/examples)
- Alternatively, clone the repository and build the example project `mvn clean install`, 
unpack the zip file from `knotx-example-project/acme-stack/target` to any folder
- Go to the `acme-stack` folder in the unpacked zip file and run the Knot.x
```
$> cd acme-stack
$> bin/knotx run-knotx
```

### Run docker
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

Open the browser for `http://localhost:8092/content/local/simple.html` URL to see the example is running.

[travis]:https://travis-ci.org/Knotx/knotx-example-project
[travis img]:https://travis-ci.org/Knotx/knotx-example-project.svg?branch=master

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg
