[![][license img]][license]
[![][gitter img]][gitter]


# GraphQL

This project provides an example implementation of using GraphQL with Knotx.
See the [Using GraphQL with Knot.x](http://knotx.io/tutorials/graphql-usage/2_0.html) tutorial for an in depth explanation.

It was created with [Knot.x Starter Kit](https://github.com/Knotx/knotx-starter-kit).

## Run
Build and run docker image:

```bash
$ ./gradlew build-docker 
$ docker run -p8092:8092 knotx-example/graphql-api
```

## Endpoints

The application exposes two endpoints:

- `/healthcheck` - an example health check
- `/api/graphql` - endpoint consuming GraphQL queries, the point of this example

## Queries

GraphQL schema can be found [here](modules/books/src/main/resources/books.graphqls). Example query asking for all possible properties:

```graphql
{
    book(id: "q5NoDwAAQBAJ") { #google api book id
        title
        publisher
        authors
    }
    books(match: "java") {
        title
        publisher
        authors
    }
}
```

It can be used, for example, in Postman. Choose the predefined `GraphQL` body type and paste the query. 

The same request in curl:

```bash
curl -i -H 'Content-Type: application/json' -X POST -d '{"query": "{book(id: \"q5NoDwAAQBAJ\") {title publisher authors} books(match: \"java\") {title publisher authors}}"}' http://localhost:8092/api/graphql
```

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg

[gitter]:https://gitter.im/Knotx/Lobby
[gitter img]:https://badges.gitter.im/Knotx/knotx-extensions.svg


