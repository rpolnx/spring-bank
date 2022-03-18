# Spring Bank Challenge

## Introduction
    This project was a challenge to build a system that simulates a bank using microservice architecture. Some of the rules are to build it using java, microservices, async processing, error's handler, queue and good code practice.

## Technologies

- Java 11 using springboot version 2.3.3
- PostgreSQL
- RabbitMQ as message broker system
- Feign - REST client
- Flyway - migration handler
- Logback - log ouput format
- Unit and integrated tests using spring-test, mockito e junit 5
- Docker e Docker-compose

## Setup

### Dependencies

```bash
$ mvn clean install
```

### Docker Stack

```bash
$ docker-compose up -d
```

### Run tests

```bash
$ mvn test
```

### Run application (context is from rootfolder)

```bash
$ java -jar customer/*.jar 
$ java -jar account/*.jar
$ java -jar service/*.jar
```

# Folder structure and architecture

## Modules Structure:
- common   - utilities, configuration, DTO common classes
- customer - microservice responsible for customer domain 
- account  - microservice responsible for account domain
- service  - microservice responsible for others bank services (credit/debit card, overdraft limit etc)

## Internal structure:
`config` - application config files

`controller` - REST in/out data controller and basic valitation

`external` - External communication: Message out, repositories adapters, rest integration

`listener` - Message in data controller from queues

`model` - Value objects, models, entities

`service` - Business layer


## Database relationship
- Customer microservice

![Customer microservice](https://github.com/rodrigorpo/spring-bank/blob/master/images/customer_ms.png)

- Account microservice

![Account microservice](https://github.com/rodrigorpo/spring-bank/blob/master/images/account_ms.png)

- Service microservice

![Service microservice](https://github.com/rodrigorpo/spring-bank/blob/master/images/service_ms.png)


## Picture from Architecture

![Architecture](https://github.com/rodrigorpo/spring-bank/blob/master/images/architecture-example.jpg)


# Final considerations
- Collection from insomnia rest client routes on folder collections
- Run the three microservices and docker-compose stack simultaneously to test application
- The customer, account and service microservices are running on ports 8080, 8081 and 8082 respectively.