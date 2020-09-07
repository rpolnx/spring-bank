FROM openjdk:11-jdk-slim as customer
WORKDIR /usr/app
COPY target/*.jar ./app.jar
COPY logback-spring.xml .
COPY wait-for-it.sh .

ENTRYPOINT java -jar app.jar