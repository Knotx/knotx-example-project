# Docker Image with HSQLDB instance
Docker image with simple HSQLDB database instance and sample data. It helps to demonstrate how to integrate
with other datasource without Web API 

## Prerequisites
- Docker

### Run 
Build image from dockerfile being in the `acme-sample-database` folder
```
$> docker build -t acme/sample-database .
```
Run HSQLDB container
```
$> docker run -p9001:9001 acme/sample-database
```


### How to verify
You can configure the database connection in any client - for instance intelij datasource configuration
```
URL: jdbc:hsqldb:hsql://localhost:9001/
user: SA
pass: (keep empty)
```
