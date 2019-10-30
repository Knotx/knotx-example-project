[![][license img]][license]
[![][gitter img]][gitter]

# API Gateway: Developer API Console

This project provides an example for more advanced usages of the OpenAPI standard.
// TODO provide link to the tutorial

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

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg

[gitter]:https://gitter.im/Knotx/Lobby
[gitter img]:https://badges.gitter.im/Knotx/knotx-extensions.svg


