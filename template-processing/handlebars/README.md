# Template processing example

## Run
Build first docker image
```bash
./gradlew build-docker
```

Run Knot.x instance and example data services (Web API and Content Repository) in a single node Docker Swarm:
```bash
docker stack deploy -c template-processing.yml template-processing
```

### Final page

http://localhost:8092/content/payment.html

### Web API
**User details**
- http://localhost:3000/user

**Payment APIs**
- http://localhost:3000/paypal/verify
- http://localhost:3000/payu/active
- http://localhost:3000/creditcard/allowed

## Other
Reload Wiremock after config changes:
```bash
docker service update --force template-processing_webapi
```

Restart Knot.x:
```bash
docker service update --force template-processing_knotx
```
