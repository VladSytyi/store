FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/*.jar store.jar
COPY application.yaml application.yaml
EXPOSE 8080
LABEL maintainer="Vladislav Sytyi"
ENTRYPOINT ["java","-jar","/store.jar"]
