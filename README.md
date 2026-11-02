# OCPPConfigurationServer

[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)
[![Package - OpenJDK](https://img.shields.io/badge/openjdk-%3E=21.0.1-blue?logo=openjdk&logoColor=white)
](https://openjdk.org/ "Go to OpenJDK website")
[![Package - Maven](https://img.shields.io/badge/maven-%3E=3.9.6-blue?logo=mvn&logoColor=white)
](https://maven.apache.org/ "Go to Maven website")
[![Package - NPM](https://img.shields.io/badge/npm-%3E=10.2.3-blue?logo=npm&logoColor=white)
](https://www.npmjs.com/ "Go to NPM website")
[![Package - NodeJS](https://img.shields.io/badge/nodejs-%3E=20.10.0-blue?logo=nodejs&logoColor=white)
](https://nodejs.org/en "Go to NodeJS website")

## Overview

A web application to update or configure a charge point with OCPP protocol.

## Requirements

### Back

- OpenJDK 21.0.1 ;
- Maven 3.9.6.

### Front

- NPM 10.2.3 ;
- NodeJS 20.20.0.

## Installation

TODO : Explain the installation process !

## Running locally with an in-memory database (easiest)

The `local` Spring profile uses an in-memory H2 database in PostgreSQL compatibility mode:
tables are created automatically from the JPA entities and seeded with one charge point
(serial `ACE0272306`, vendor `Alfen`) and test users (`admin@local`, `editor@local`,
`viewer@local`, password `admin` for all). The H2 console is available at
http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:ocpp`).

```bash
mvn package -DskipTests
SPRING_PROFILES_ACTIVE=local WEBSOCKET_URL=0.0.0.0 WEBSOCKET_PORT=8765 \
  java -jar service/target/service-0.0.1-SNAPSHOT.jar
```

The OCPP websocket server starts with the application on `WEBSOCKET_URL:WEBSOCKET_PORT`.
Then emulate a charge point with:

```bash
python3 doc/chargepoint_emulator.py --url ws://127.0.0.1:8765 --serial ACE0272306 --vendor Alfen
```

## Running program (with a real PostgreSQL database)
You must set those environment variables in IntelliJ in the edit project configuration:
- POSTGRESQL_DB_ADDRESS=<*database url, e.g. jdbc:postgresql://localhost:5432/ocpp*>;
- POSTGRESQL_DB_USERNAME=<*database username*>;
- POSTGRESQL_DB_PASSWORD=<*database password*>;
- SPRING_JPA_HIBERNATE_DDL_AUTO=<*e.g. update to auto-create tables, none otherwise*>;
- FINAL_WS_SERVER_ADDRESS=<*next supervisor server address*>;
- WEBSOCKET_PORT=<*websocket port*>;
- WEBSOCKET_URL=<*websocket ip address*>;

## Running check style
You should install the plugin in IntelliJ CheckStyle-IDEA and set the *checkstyle.xml* in the plugin configuration.
Then check style could be executed locally through the plugin menu.

## Running tests

## Running the charge points generator
The python script is used to generate charge points and status into the database.   
You will need python3 as well as pymysql installed : ```pip install pymysql```.   
You can run it by typing ```python3 generator.py -h```.  
It will display information on the program and the arguments.




