FROM maven:3.8.5-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar

ARG EUREKA_USER=eureka-microservices-admin
ARG EUREKA_PASS=akeruesecivresorcimnimda
ARG EUREKA_SERVER=127.0.0.1
ARG RABBITMQ_SERVER=rabbitmq

ENTRYPOINT java -jar -Dspring.profiles.active=prod app.jar