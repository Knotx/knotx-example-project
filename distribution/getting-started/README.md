
# Getting started with Docker example
This project is a result of completing either [Getting Started with Knot.x Stack](http://knotx.io/tutorials/getting-started-with-knotx-stack/2_0/) or [Getting started with Docker](http://knotx.io/tutorials/getting-started-with-docker/2_0/) tutorials.

This example is built using the [Starter Kit](https://github.com/Knotx/knotx-starter-kit) template project.

## How to build & run
    
### Stack

#### Prerequisites
- JDK 8
- Linux or OSX bash console (for Windows users we recommend using e.g. Ubuntu with [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10))

#### Run
```
    # build the Knot.x stack
$ ./gradlew build

    # unzip the distribution
$ cd build/distributions
$ unzip knotx-stack-<version of your project>.zip

    # run Knot.x
$ cd knotx
$ chmod +x bin/knotx
$ bin/knotx run-knotx
```   

and validate url:
- [localhost:8092/api/hello](http://localhost:8092/api/hello)
    
### Docker

#### Prerequisites
- JDK 8
- Docker

#### Run
```
$ ./gradlew build-docker
```

The [build-docker](https://github.com/Knotx/knotx-starter-kit#build--validate-docker-image) builds and validates the custom project Docker image.

Then, start Docker container:
```
$ docker run -p8092:8092 knotx/knotx-docker-tutorial
```

and validate url:
- [localhost:8092/api/hello](http://localhost:8092/api/hello)

## What is the delta between [Starter Kit](https://github.com/Knotx/knotx-starter-kit)?

- `hellohandler` module that handles our requests under `/api/hello` and prints a Hello World message
- `health-check` module is modified to rely on `/api/hello`
- `functional` tests also rely on `/api/hello` 
- this code was cleaned from unnecessary example modules and configurations (**not covered in tutorials**)
