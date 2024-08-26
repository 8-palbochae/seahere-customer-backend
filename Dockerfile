FROM openjdk:11.0-slim

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd", "backend-0.0.1-SNAPSHOT.jar"]
