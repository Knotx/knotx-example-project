[![Build Status](https://dev.azure.com/knotx/Knotx/_apis/build/status/Knotx.knotx-example-project?branchName=master)](https://dev.azure.com/knotx/Knotx/_build/latest?definitionId=15&branchName=master)

# Knot.x Examples
Examples show how you can customize/extend [Knot.x Stack](https://github.com/Knotx/knotx-stack) in 
a real project. They use Gradle to build all custom modules and prepare a distribution 
containing custom configurations and modules. 

We recommend to start with [Knot.x Starter Kit](https://github.com/Knotx/knotx-starter-kit) which
builds Docker image. Please note that using Docker is not mandatory.

## Topics covered in our examples:

### Distribution

Within this module we present how to distribute Knot.x

|Topic|Description|Used technologies|Tutorial|Source code|
|-----|-----------|-----------------|--------|-----------|
|Knot.x stack|Basic usage of [fragmentsHandler](https://github.com/Knotx/knotx-fragments/tree/master/handler) with a simple graph that uses data from external API and combines them with a static HTML provided by [fsRepoConnectionHandler](https://github.com/Knotx/knotx-repository-connector/tree/master/fs) |[fsRepoConnectionHandler](https://github.com/Knotx/knotx-repository-connector/tree/master/fs), [fragmentsHandler](https://github.com/Knotx/knotx-fragments/tree/master/handler), Handlebars|[Getting Started with Knot.x Stack](http://knotx.io/tutorials/getting-started-with-knotx-stack/2_0/)|`./distribution/knotx-stack`| 
|Docker|This module covers the same topics as above, but we are embedding the Knot.x stack inside Docker image|[fsRepoConnectionHandler](https://github.com/Knotx/knotx-repository-connector/tree/master/fs), [fragmentsHandler](https://github.com/Knotx/knotx-fragments/tree/master/handler), Handlebars, Docker|[Getting Started with Docker](http://knotx.io/tutorials/getting-started-with-docker/2_0/)|`./distribution/docker`| 


### [API Gateway](https://github.com/Knotx/knotx-example-project/tree/master/gateway-api)
This module presents how Knot.x can be used as API Gateway.

|Topic|Description|Used technologies|Tutorial|Source code|
|-----|-----------|-----------------|--------|-----------|
|Getting started|An introduction to using Knot.x for API implementation|[Action](https://github.com/Knotx/knotx-fragments/tree/master/handler/api), [fragmentsHandler](https://github.com/Knotx/knotx-fragments/tree/master/handler), Docker Swarm, [WireMock](http://wiremock.org/)|[Configurable API gateway](http://knotx.io/tutorials/configurable-api-gateway/2_0/)|`./api-gateway/getting-started`| 
|Securing your API |Simple usage of popular authentication technologies to secure your API|Docker, OAuth 2.0, OpenId Connect, JWT|*coming soon*|*coming soon*| 

### Template Processing
This module presents the aspects of template processing.

|Topic|Description|Used technologies|Tutorial|Source code|
|-----|-----------|-----------------|--------|-----------|
|Getting started|Basic usage of [fragmentsHandler](https://github.com/Knotx/knotx-fragments/tree/master/handler) with a simple graph that uses data from external API and combines them with a static HTML provided by [fsRepoConnectionHandler](https://github.com/Knotx/knotx-repository-connector/tree/master/fs) |[fsRepoConnectionHandler](https://github.com/Knotx/knotx-repository-connector/tree/master/fs), [fragmentsHandler](https://github.com/Knotx/knotx-fragments/tree/master/handler), Docker, Handlebars|[Getting Started with Docker](http://knotx.io/tutorials/getting-started-with-docker/2_0/)|`./distribution/docker`| 

[license]:https://github.com/Cognifide/knotx/blob/master/LICENSE
[license img]:https://img.shields.io/badge/License-Apache%202.0-blue.svg

[gitter]:https://gitter.im/Knotx/Lobby
[gitter img]:https://badges.gitter.im/Knotx/knotx-extensions.svg
