# Spring Bank Challenge

## Introdução
Esse projeto faz parte de um desafio de uma aplicação de microsservicos para criar um sistema de banco. 
Algumas das regras são sua construção em java, uso de microsserviços, utilização de processamento assíncrono, 
tratamento de erros, uso de filas e boas práticas de código.

## Tecnologias

- Java 11 com springboot versão 2.3.3
- PostgreSQL
- RabbitMQ como mensageria
- Feign para integração REST
- Flyway para controle de migrations
- Logstash para controle de log
- Testes unitários e integrados spring-test, mockito e junit 5
- Docker e Docker-compose

## Setup

### Instalar dependências

```bash
$ mvn clean install
```

### Subir o docker-compose com as dependências requeridas

```bash
$ docker-compose up
```

### Rodar testes

```bash
$ mvn test
```

### Rodar aplicação (referência da pasta raíz)

```bash
$ java -jar customer/*.jar 
$ java -jar account/*.jar
$ java -jar service/*.jar
```

## Estrutura de pastas e Arquitetura

### Nível principal:
- common   - módulo com utilitários, configurações e classes de DTO comuns
- customer - microsservico responsável pelo domínio do cliente 
- account  - microsservico responsável pelo domínio da conta
- service  - microsservico responsável pelo domínio de serviços (cartão e cheque especial)

Estrutura interna:
config - arquivos de configurações gerais da aplicação

`controller` - camada de integração rest inicial

`external` - camada responsável por toda comunicação externa (integração rest, publicar em filas e comunicar com banco)

`listener` - camada de integração de mensageria inicial

`model` - Entidades, objetos de valor, enums e suas classes auxiliares de suas criações

`service` - Camada responsável por realizar a regra de negócio

