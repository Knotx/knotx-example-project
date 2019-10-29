# API Gateway: caching

## Run
Build first docker image
```
$ gradlew clean build
```

Deploy the **API Gateway** (Knot.x HTTP Server) and the **target `http://localhost:3000/product/id` API** 
(serving JSON data over a RESTful HTTP API) with Docker Swarm:
```
$ docker swarm init
$ docker stack deploy -c api-cache.yml api-cache
```

Check if all containers are up and running:
```
$ docker ps
```

## API Gateway
 - [http://localhost:8092/product/id](http://localhost:8092/product/id) - API Proxy with caching
 
## Target (external) APIs (Wiremock)
- [http://localhost:3000/product/id](http://localhost:3000/product/id)

## Other
Remove stack:
```
$ docker stack rm api-cache
```
Display API Gateway logs:
``` 
$ docker service logs -f api-cache_knotx
```
Reload the target API after Wiremock configuration changes:
```
$ docker service update --force api-cache
```