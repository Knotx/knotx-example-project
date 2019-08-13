# Getting started

Complete project implementation from tutorial [Getting Started with Knot.x Stack](http://knotx.io/tutorials/getting-started-with-knotx-stack/). Gralde `build` task downloads Knot.x Stack for you and override the configuration files with content from this repository.

## Run
Build first
```
$ gradlew clean build
```

Unzip `build/distribution/knotx-stack-version.zip`

Run

```
$ bin/knotx run-knotx
```

###Final page

http://localhost:8092/content/books.html
