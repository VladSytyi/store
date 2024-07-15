FROM openjdk:21-jdk-slim

# Set default environment variables (will be overridden by --env-file)
ENV JDBC_URL=jdbc:postgresql://localhost:5432/store?currentSchema=store
ENV JDBC_USERNAME=user
ENV JDBC_PASSWORD=pw1
ENV REPO_PAGE_SIZE=5
ENV CACHE_HOST=localhost
ENV CACHE_PORT=6379
ENV SPRING_RABBITMQ_HOST=localhost
ENV SPRING_RABBITMQ_PORT=5672
ENV SPRING_RABBITMQ_USERNAME=user
ENV SPRING_RABBITMQ_PASSWORD=pw2

VOLUME /tmp
COPY target/*.jar store.jar
EXPOSE 8080
EXPOSE 9090
LABEL maintainer="Vladislav Sytyi"
ENTRYPOINT ["java","-jar","/store.jar"]
