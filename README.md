# VUTTR (Very Useful Tools to Remember)

A aplicação é um simples repositório para gerenciar ferramentas com seus respectivos nomes, links, descrições e tags

---

## Index
- [Requirements](vuttr#requirements)
- [Environment Setup](vuttr#environment-setup)
- [Configure Database](vuttr#configure-database)
- [Running the Application](vuttr#running-the-application)
- [API Docs](vuttr#api-docs)

---

## Requirements

* JDK 11
* Gradle
* Mongodb

---

## Environment Setup

---

### Configure Database

Start oracle database docker

```sh
docker-compose up -d
```

Be sure that container has been started [state should be: up (healthy)]
```sh
docker-compose ps
```

To accompain the log
```sh
docker-compose logs -f
```

---

### Running the Application

Run the following command to run server
```sh
gradle bootRun
```

The server is running on `http://localhost:3000`

---

### Running lint check

Run the following command to run lint check
```sh
gradle check
```

The reports will generate in `build/reports/klint`

---

### Building native image

Run the following command to build native docker image
```sh
gradle bootBuildImage
```

After complete will be possible execute application running the command:
```sh
docker-compose up -d api
```

---

### Creating local environment variables 

Run the following command to create environment variables
```sh
source env.sh
```

---

### API Docs

There is an openapi documentation in `src/main/resources/openapi/api.yml` and a collection postman in `postman/Tools.postman_collection.json`