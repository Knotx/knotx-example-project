# Developer API Console

This project provides an example for a more advanced uses of the OpenAPI standard. In depth description can be found in [this tutorial](https://knotx.io/tutorials/openapi-and-swagger-ui/2_0).

It was created with [Knot.x Starter Kit](https://github.com/Knotx/knotx-starter-kit).

## Run
Build and run docker image:

```bash
$ ./gradlew build-docker 
$ docker run -p8092:8092 knotx-example/openapi
```

## Endpoints

The application exposes multiple endpoints. All of them can be viewed and tested in the Swagger UI app at the [/swagger/ui](http://localhost:8092/swagger/ui#/) endpoint.

Endpoints include:
- `/swagger/spec` - serves `openapi.yaml` file as a static resource (for consumption by swagger-ui app)
- `/swagger/ui` - the swagger-ui app
- multiple mocked endpoints, for presentation purposes only



