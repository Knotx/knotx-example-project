# API gateway: composing many APIs
Knot.x API Proxy pattern that lets you combine results from multiple APIs into a single response.

![APIs integration scenario](./assets/img/composing-many-apis.png)

The above diagram presents the APIs composition logic. First, we need to get some user details. Then 
we use this data during payments APIs invocations. When all payment APIs respond, we do some 
postprocessing logic and return composed JSON data.

## Run
Build first a Docker image:
```
$ gradlew clean build
```

Deploy an **API gateway** (Knot.x HTTP Server) and **User & Payments APIs** (serving JSON data 
over a RESTful HTTP API) with Docker Swarm:
```
$ docker swarm init
$ docker stack deploy -c composing-many-apis.yml composing-many-apis
```

Check if all containers are up and running:
```
$ docker ps
```

## API gateway
 - [http://localhost:8092/api/v1/payments](http://localhost:8092/api/v1/payments) - a routing handler 
 implementation using Vert.x Web Client to fetch JSON data from RESTful HTTP APIs and RxJava to manage 
 asynchronous programming challenges
 - [http://localhost:8092/api/v2/payments](http://localhost:8092/api/v2/payments) - a similar 
 approach like in the `v1` API version, a circuit breaker pattern implemented
 - [http://localhost:8092/api/v3/payments](http://localhost:8092/api/v3/payments) - a solution 
 utilizing [Configurable Integration](https://knotx.io/blog/configurable-integrations/) features,
 defines a API composition logic in a form of manageable graph
 
 
## Target (external) APIs

**User API**
- http://localhost:3000/user

**Payments APIs**
- http://localhost:3000/paypal/verify
- http://localhost:3000/payu/active
- http://localhost:3000/creditcard/allowed

## Other
Remove stack:
```
$ docker stack rm composing-many-apis
```
Display Knot.X logs
``` 
$ docker service logs -f composing-many-apis_knotx
```
Reload Wiremock after config changes:
```
$ docker service update --force composing-many-apis
```