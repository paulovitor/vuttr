# VUTTR (Very Useful Tools to Remember)

A aplicação é um simples repositório para gerenciar ferramentas com seus respectivos nomes, links, descrições e tags

---

## Index
- [Requirements](#markdown-header-requirements)
- [Environment Setup](#markdown-header-environment-setup)
- [Configure Database](#markdown-header-configure-database)
- [Running the Application](#markdown-header-running-the-application)
- [API Docs](#markdown-header-api-docs)

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

### API Docs

There is an openapi documentation in `src/main/resources/openapi/api.yml` and a collection postman in `postman/Tools.postman_collection.json`