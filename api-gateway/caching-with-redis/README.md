[![][license img]][license]
[![][gitter img]][gitter]

# Redis Cache

This project provides an example Redis cache implementation in Knot.x.

It was created with [Knot.x Starter Kit](https://github.com/Knotx/knotx-starter-kit).

## Run
Build and run docker swarm:

```bash
./gradlew build
docker swarm init
docker stack deploy -c redis-handler.yml redis-handler
```

## Endpoints

There is one endpoint `GET /book` that calls Google Books API. It's value is cached for 20 seconds.

You can check the logs with 

```bash
docker service logs redis-handler_knotx
```

Open [localhost:8092/book](http://localhost:8092/book). In the logs there should be:

```
No valid cache for key: the-book, calling the action
New value cached under key: the-book for 20 seconds
```

After refresh the value will be retrieved from Redis and Google Books API won't be called:

```
Retrieved value from cache under key the-book
```

And after waiting 20 seconds and refreshing the Google API will be called again, because the cache will be invalidated:

```
No valid cache for key: the-book, calling the action
New value cached under key: the-book for 20 seconds
```

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg

[gitter]:https://gitter.im/Knotx/Lobby
[gitter img]:https://badges.gitter.im/Knotx/knotx-extensions.svg


