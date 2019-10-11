
# Getting started with Docker example
This project is a result of completing [Getting started with Docker](http://knotx.io/tutorials/getting-started-with-docker/2_0/) tutorial.

This example is built using the [Starter Kit](https://github.com/Knotx/knotx-starter-kit) template project.

## How to build & run
    
```
$> ./gradlew build-docker
```

The [build-docker](https://github.com/Knotx/knotx-starter-kit#build--validate-docker-image) task comes from Starter Kit.

Then, start Docker container:
```
docker run -p8092:8092 knotx/knotx-docker-tutorial
```

and validate urls:
- [localhost:8092/api/hello](http://localhost:8092/api/hello)

## What is the delta between [Starter Kit](https://github.com/Knotx/knotx-starter-kit)?

- `hellohandler` module that handles our requests under `/api/hello` and prints a Hello World message
- `health-check` module is modified to rely on `/api/hello`
- `functional` tests also rely on `/api/hello` 
- Example APIs have been removed to simplify the code (**not covered in tutorial**)
