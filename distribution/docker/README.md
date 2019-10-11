
# Getting started with Docker example
This project is a result of completing [Getting started with Docker](http://knotx.io/tutorials/getting-started-with-docker/2_0/) tutorial.

This example is built using the [Starter Kit](https://github.com/Knotx/knotx-starter-kit) template project.

## How to build & run
    
```
$> ./gradlew build-docker
```

The [build-docker](https://github.com/Knotx/knotx-starter-kit#build--validate-docker-image) builds and validates the custom project Docker image.

Then, start Docker container:
```
docker run -p8092:8092 knotx/knotx-docker-tutorial
```

and validate url:
- [localhost:8092/api/hello](http://localhost:8092/api/hello)

## What is the delta between [Starter Kit](https://github.com/Knotx/knotx-starter-kit)?

- `hellohandler` module that handles our requests under `/api/hello` and prints a Hello World message
- `health-check` module is modified to rely on `/api/hello`
- `functional` tests also rely on `/api/hello` 
- this code was cleaned from unnecessary example modules and configurations (**not covered in tutorial**)
