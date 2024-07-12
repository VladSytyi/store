from maven:3.9.8-eclipse-temurin-21 as build
WORKDIR /app
COPY . /app
RUN mvn clean package
from openjdk:21-jdk-slim

# Copy the JAR file from the build image
COPY --from=build /app/target/*.jar store.jar
EXPOSE 8080
EXPOSE 9090
ENTRYPOINT ["java","-jar","/store.jar"]