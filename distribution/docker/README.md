
# Getting started with Docker example
This project is a result of completing [Getting started with Docker](http://knotx.io/tutorials/getting-started-with-docker/2_0/) tutorial.

## How to build & run
    
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
docker run -p8092:8092 knotx/knotx-docker-tutorial
```

and validate urls:
- [localhost:8092/api/hello](http://localhost:8092/api/hello)

## What is the delta between  starter-kit

- `hellohandler` module that handles our requests under `/api/hello` and prints a Hello World message
- Example APIs have been removed to simplify the code (**not covered in tutorial**)
- `health-check` module is modified to rely on `/api/hello` instead of our example APIs which have been removed (**not covered in tutorial**)
- functional tests also rely on `/api/hello` (**not covered in tutorial**)
