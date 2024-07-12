FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/*.jar store.jar
EXPOSE 8080
EXPOSE 9090
LABEL maintainer="Vladislav Sytyi"
ENTRYPOINT ["java","-jar","/store.jar"]
