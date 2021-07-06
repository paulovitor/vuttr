# VUTTR (Very Useful Tools to Remember)

The application is a simple repository to manage tools with your respective names, links, descriptions and tags


## Index

- [Requirements](#requirements)
- [How to use](#how-to-use)
    - [Run application via Docker](#run-application-via-docker)
    - [Run application via bootJar](#run-application-via-bootjar)
    - [Run application via Intellij](#run-application-via-intellij)
    - [Run check lint](#run-check-lint)
    - [Run unit tests](#run-unit-tests)
- [API Docs](#api-docs)


## 📦 Requirements

* JDK 11
* Gradle
* Docker


## 🚀 How to use

### Run application via Docker

1) Build spring boot docker image

```sh
gradle bootBuildImage
```

The gradle spring boot plugin will create vuttr:0.0.1-SNAPSHOT docker image

2) Start docker-compose command

```sh
docker-compose up -d
```

The docker-compose will initialize api and mongodb containers

3) Be sure that container has been started [state should be: up (healthy)]
```sh
docker-compose ps
```

4) To accompain the log
```sh
docker-compose logs -f
```

The server is running on `http://localhost:3000`

### Run application via bootJar

1) The mongodb docker container need is running

```sh
docker-compose up -d mongo
```

2) Creating local environment variables
```sh
source .env.sh
```

3) Start application server
```sh
gradle bootRun
```

The server is running on `http://localhost:3000`

### Run application via Intellij

1) The mongodb docker container need is running

```sh
docker-compose up -d mongo
```

2) [Set up environment variables in Intellij](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html)

![Set up environment variables in Intellij](docs/assets/environment-variable-intellij.png?raw=true "Set up environment variables in Intellij")

3) Start application server

![Start application server](docs/assets/start-server-intellij.png?raw=true "Start application server")

The server is running on `http://localhost:3000`

### Run check lint

Run the following command to run lint check
```sh
gradle check
```

The reports will generate in `build/reports/klint`

### Run unit tests

Run the following command to run unit tests
```sh
gradle test
```

The reports will generate in `build/reports/tests`


## 📄 API Docs

1) The openapi documentation can be access in: `http://localhost:3000/swagger-ui/index.html`

2) The postman collection is available in: `postman/Tools.postman_collection.json`