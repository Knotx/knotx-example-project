FROM java:openjdk-8-jre-alpine

RUN apk add --update bash curl unzip

RUN curl http://iweb.dl.sourceforge.net/project/hsqldb/hsqldb/hsqldb_2_4/hsqldb-2.4.1.zip -o /tmp/hsqldb-2.4.1.zip \
    && unzip /tmp/hsqldb-2.4.1.zip -d /opt \
    && ln -s /opt/hsqldb-2.4.1/hsqldb /opt/hsqldb \
    && rm -f /tmp/hsqldb-2.4.1.zip

WORKDIR /opt/hsqldb/data

COPY books.script .
COPY server.properties .

CMD java -cp /opt/hsqldb/lib/hsqldb.jar org.hsqldb.server.Server

EXPOSE 9001
