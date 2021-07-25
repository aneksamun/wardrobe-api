# Wardrobe API

![Build status](https://github.com/aneksamun/wardrobe-api/actions/workflows/scala.yml/badge.svg)

The purpose of the system is to allow the user to store and retrieve clothing, clothing categories, and outfits of clothing. As such, it is letting user to:
* Search for clothes by name
* Return a list of clothes, their categories & which outfits they're in
Upload a CSV file containing clothes and clothing categories in the following format:
```
name, category
iSwim Summer Bikini, Bikinis
iWalk Blue Jeans, Trousers
iWalk Dress Trousers, Trousers
```
* Tag clothes as part of an outfit

#### Configuration

The service can customised by changing following settings

| Setting          | Description                                            | Default value |
|------------------|--------------------------------------------------------|---------------|
| SERVER_HTTP_PORT | Server port                                            | 8080          |
| DB_NAME          | Database name                                          | wardrobe      |
| DB_HOST          | Database server host                                   | localhost     |
| DB_PORT          | Database server port                                   | 5432          |
| DB_USER          | Database user                                          | user          |
| DB_PASSWORD      | Database user password                                 | 1234          |
| DB_CREATE_SCHEMA | Whether to create a database schema on the system run? | true          |

#### How to?

Use given `sbt` commands to compile and test project.

* `sbt compile` - compiles project
* `sbt test` - runs unit tests
* `sbt docker` - builds a Docker image
* `sbt it:test` - runs Integration Tests
* `sbt run` - runs project

Execute `docker-compose up -d` to start service dependencies as Postgres database.
  
#### Endpoints

Upon service run following set of the actions are available:
 
| Path                           | Method | Response | Description                                                                                    |
| ------------------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------- |
| /api/clothes                   | POST   | 200      | Use endpoint to upload CSV file containing clothes and categories                              |
| /api/clothes/:name             | GET    | 200      | Use endpoint to search clothes by name                                                         |
| /api/clothes?offset=0&limit=10 | GET    | 200/404  | Use endpoint to browse clothes. Apply an `offset` and `limit` to page results                  |
| /api/clothes/:name/outfit      | PUT    | 200/500  | Use endpoint to tag clothes. The outfit must be provided in the body as `{ "name": "outfit" }` |

### Technology stack
- [scala 2.13.6](http://www.scala-lang.org/) as the main application programming language
- [http4s](https://http4s.org/) typeful, functional, streaming HTTP for Scala
- [doobie](https://tpolecat.github.io/doobie/) a pure functional JDBC layer for Scala
- [cats](http://typelevel.org/cats/) to write more functional and less boilerplate code
- [cats-effect](https://github.com/typelevel/cats-effect) The Haskell IO monad for Scala
- [pureconfig](https://pureconfig.github.io/) for loading configuration files
- [refined](https://github.com/fthomas/refined) for type constraints avoiding unnecessary testing and biolerplate
- [circe](https://circe.github.io/circe/) a JSON library for Scala
- [scalatest](http://www.scalatest.org/) and [ScalaCheck](https://www.scalacheck.org/) for *unit and property based testing*
- [testcontainers](https://github.com/testcontainers/testcontainers-scala) to run system dependant services for Integration Testing purposes 
- [sttp](https://sttp.softwaremill.com/en/latest/) the Scala http client

