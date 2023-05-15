Simple implementation of a cache to store FX instrument prices.

This project depends on java 17, to make it work with java 11,
spring boot needs to be downgraded latest from 2.x line.

To run web server simply execute:

```
mvn spring-boot:run
```

Server will be listening on `localhost:8080`

You can retrieve the latest available price by:

```
curl -v  -H "Accept: application/json" "localhost:8080/price/latest?instrumentName=EUR/JPY"
```

You can also load some sample data using the following command:

```
curl -X POST -H "Content-Type: text/plain" "http://localhost:8080/price/load" \
    -d "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001
107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002
108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002
109, GBP/USD, 1.2499,1.2561,01-06-2020 12:01:02:100
110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110
"
```

If you do not have `curl` installed, you can use any other REST client.