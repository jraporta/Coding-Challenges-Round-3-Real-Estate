FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/realestate-0.0.1-SNAPSHOT.jar /app/realestate-0.0.1-SNAPSHOT.jar

ENV MYSQL_URL=jdbc:mysql://mysql:3306/realestate
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=root

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "/app/realestate-0.0.1-SNAPSHOT.jar"]