[![][license img]][license]
[![][gitter img]][gitter]


# Gateway-API


## Run
Build first docker image
```
$ gradlew clean build
```

Run Knot.x instance and example Web API services (User details, Payment API) in a single node Docker Swarm:
```
$ docker swarm init

$ docker stack deploy -c gateway-api.yml gateway-api
```

## Knotx links

 - http://localhost:8092/api/v1/payments - basic api implementation.
 - http://localhost:8092/api/v2/payments - circle breaker implementation
 - http://localhost:8092/api/v3/payments - knot.x fragments, task and actions configurable implementation
 
## External Services

**User details**
- http://localhost:3000/user

**Payment APIs**
- http://localhost:3000/paypal/verify
- http://localhost:3000/payu/active
- http://localhost:3000/creditcard/allowed

## Other
Remove stack:
```
$ docker stack rm gateway-api
```
Display Knot.X logs
``` 
 docker service logs -f gateway-api_knotx
```
Reload Wiremock after config changes:
```
docker service update --force gateway-api_webapi
```



[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg

[gitter]:https://gitter.im/Knotx/Lobby
[gitter img]:https://badges.gitter.im/Knotx/knotx-extensions.svg


