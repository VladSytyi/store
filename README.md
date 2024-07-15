# store
Demo project for interview

## Prerequisites
- Docker
- Docker Compose

### Installing

1. Clone the repository
2. Execute `docker-compose up -d` to startup the environment
3. In the projects classpath Run `./mvnw clean install` to build the project
4. Build docker image `docker build -t store:0.0.1 .`
5. Run the docker image `docker run --env-file .env -p 8080:8080 store:0.0.1`.  
6. To connect to Swagger, open 'http://localhost:8080/test/api.html' in your browser. APi secured with basic auth. Use `user` and `pw3` as credentials.
7. The database already has some data. You can check it by connecting to the database or see init.sql file.
8. To connect to PGAdmin, open 'http://localhost:5050' in your browser ( use `host.docker.internal` as host to connect to the database )
9. To connect to RabbitMQ, open 'http://localhost:15672' in your browser
10. To check HTTP trace bean works see the logs, it will start with `store.listener.RabbitMQListener  `


Default Credentials: 
- App:
  - To authenticate in app use : username: `user` password: `pw3`. You can rewrite it in the `.env` file. see `AUTH_USER` and `AUTH_PASSWORD`
- Postgres:
  - username: `user`
  - password: `pw1`
  - database: `store`
  - schema: `store`  
  Those credentials are in the `.env` file. see `DB_USER`, `DB_PASSWORD`, `DB_NAME`, `DB_SCHEMA`
- RabbitMQ:
  - username: `user`
  - password: `pw2`
  Those credentials are in the `.env` file. see `RABBIT_USER`, `RABBIT_PASSWORD`. if you want to change it please change `SPRING_RABBITMQ_USERNAME`, `SPRING_RABBITMQ_PASSWORD`, `SPRING_RABBITMQ_HOST`, `SPRING_RABBITMQ_PORT` in the `.env` file.
  - Also change rabbit creds in rabbit-definition.json


