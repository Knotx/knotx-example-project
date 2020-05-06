# Knot.x Fragments Chrome Extension demo

## Run
Run Knot.x instance and example data services (Web API and Content Repository) in a single node Docker Swarm:
```
docker stack deploy -c knotx-extension-demo.yml extension
```

## Examples

**Payments**
Renders a page that is processed by `content-get` operation.

- Example final page: http://localhost:8092/content/payments.html

### Web API
**User details**
- http://localhost:3000/user
- http://localhost:3000/payments
- http://localhost:3000/offers
- http://localhost:3000/delivery/options

## Other
Reload Wiremock after config changes:
```
docker service update --force extension_webapi
```

Restart Knot.x:
```
docker service update --force extension_knotx
```

### Internals
- [`rodolpheche/wiremock`](https://github.com/rodolpheche/wiremock-docker) image used to set Wiremock as a Docker container wor WebAPI service
- `httpd:2.4` image used to run Content Repository as Docker service
