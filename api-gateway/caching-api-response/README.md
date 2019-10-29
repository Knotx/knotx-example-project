[![][license img]][license]
[![][gitter img]][gitter]


# Caching API responses

## Run
Build and run docker image:

```bash
$ ./gradlew clean build
$ docker swarm init
$ docker stack deploy -c api-cache.yml api-cache
```

## Endpoints

The application exposes two endpoints:

- `:8092/product/id` - endpoint behind proxy provided by Knot.x
- `:3000/product/id` - raw endpoint provided by WireMock

The responses contain data that is unique for each and every request to WireMock. By using Knot.x and it's caching mechanisms, we can observe how responses are cached when invoking Knot.x instead of WireMock.

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg

[gitter]:https://gitter.im/Knotx/Lobby
[gitter img]:https://badges.gitter.im/Knotx/knotx-extensions.svg


