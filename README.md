# OCCPConfigurationServer

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

A web application to update or configure an electrical terminal via OCCP protocol.

## Requirements

### Back

- OpenJDK 21.0.1 ;
- Maven 3.9.6.

### Front

- NPM 10.2.3 ;
- NodeJS 20.20.0.

## Installation

TODO : Explain the installation process !


## Running program
You must set those environment variables in IntelliJ in the edit project configuration:
- DB_ADDRESS=<*test database url*>;
- DB_PASSWORD=<*test database password*>;
- DB_USERNAME=<*test database username*>;
- FS_HOST=<*ftp server address*>;
- FS_PASSWORD=<*ftp password*>;
- FS_PORT=<*ftp port*>
- FS_USER=<*ftp user*>;
- MYSQL_ADDON_USER=<*database username*>;
- MYSQL_DB_ADDRESS=<*database url*>;
- MYSQL_DB_PASSWORD=<*database password*>;
- WEBSOCKET_PORT=<*websocket port*>;
- WEBSOCKET_URL=<*websocket ip address*>;

## Running check style
You should install the plugin in IntelliJ CheckStyle-IDEA and set the *checkstyle.xml* in the plugin configuration.
Then check style could be executed locally through the plugin menu.

## Running tests

## Running the chargepoints generator
The python scriptis used to generate chargepoints and status into the database.   
You will need python3 as well as pymysql installed : ```pip install pymysql```.   
You can run it by typing ```python3 generator.py -h```.  
It will display informations on the program and the arguments.




