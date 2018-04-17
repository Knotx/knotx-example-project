[![][travis img]][travis]
[![][license img]][license]

# Running the example project

## Prerequisites
- JDK 8+

## Getting started
### Run standalone (TBD)
- Get acme zip (from maven or build)
- Unpack
- `bin/knotx run-knotx`

### Run docker
Build image from dockerfile
```
$> docker build -t acme/knotx-example .
```
Run example as deamon
```
$> docker run -d -p8092:8092 acme/knotx-example knotx-example
```

Follow logs
```
$> docker logs -f knotx-example
```

[travis]:https://travis-ci.org/Knotx/knotx-example-project
[travis img]:https://travis-ci.org/Knotx/knotx-example-project.svg?branch=master

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg
