# multiport-metrics

This examples show how to register metrics on another port and access it.

## Build the application

```shell
mvn package
```

## Run the application

```shell
java -jar target/helidon-examples-webserver-multiport.jar
```

## Exercise the application

```shell
curl http://localhost:8083/observe/metrics
...
# TYPE myCounter_total counter
myCounter_total{scope="application",} 0.0
```

Call several time `/hello` endpoint.
```shell
curl http://localhost:8080/hello
Hello!
```

Call again the metrics endpoint
```shell
curl http://localhost:8083/observe/metrics
...
# TYPE myCounter_total counter
myCounter_total{scope="application",} 3.0
```