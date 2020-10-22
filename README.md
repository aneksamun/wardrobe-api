Endpoints to test
- POST http://localhost:8080/api/clothes - posts CSV file
- GET http://localhost:8080/api/clothes/{some name} - finds clothes by name
- GET http://localhost:8080/api/clothes?offset=1&limit=2 - list all with pagination
- PUT http://localhost:8080/api/clothes/{some name}/outfit - tags clothes by setting outfit

PUT must have body
```
{
    "name": "test"
}
```

To start database you can use Docker Compose
```
docker-compose up -d
```
By the default database schema migration is set to true. 

Please check how to post file:
https://www.google.com/search?q=postman+how+to+send+multipart%2Fform-data+csv&oq=postman+how+to+send+multipart%2Fform-data+csv&aqs=chrome.0.69i59.2394j0j7&sourceid=chrome&ie=UTF-8#kpvalbx=_TDOQX_TUJt-c1fAP4Y6syAw14

###TODO:
- I haven't done tests. Sorry luck of time. Would like to use Testcontainers (will continue working on it).
- I wanted to add OpenApi page
- Paging needed to return total number

Build:
```
sbt compile
sbt run
```

Thanks.
