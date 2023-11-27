# Order Book Api
=============================

This is a REST Api based application which accepts orders / executions and adds them to an instrument specific order book.

## Design
=========
The code is organized in 3-tier design - Rest controller, service and repository.

The controller will accept immutable DTO's and are mapped to entities using MapStruct. 
The validations are done on the DTO's using annotations. If the Rest operation is successful,
a standardized JSON response (ApiMethodResponse.java) will be provided. 
In case of any errors like Bad request, request validation etc. JSON response (ApiErrorResponse.java) will be returned.
The details of these models and Api operations are available in OpenApi docs.

The repository interfaces are implemented using in memory collections. 
These can be easily switched to use JpaRepository with minimal changes in service layer.
Entity mapping -> Order book per instrument (1:1 mapping) and each order book has collection of orders and executions

## Assumptions
==============
1. List of instrument id's(1..10) are loaded during application startup from application.properties 
   and order books are initialized per instrument
2. Order / Execution quantity range, min >= 1 and max < 1000000
3. Limit order / Execution price range, min >= 0.01 and max < 100000


## Prerequisites
================

Before you begin, ensure you have the following installed:
- Open JDK - v17
- Apache Maven - 3.9.5

On Windows, check from cmd prompt that these are available on PATH.
-> java -version
-> mvn -v

If these commands are not working, you need to add installation bin directory path to system environment, variable PATH.
After these are configured correctly, you can run these commands from any folder.

## Build
========

1. Download the order-book.zip file and unzip it to a convenient directory.
2. Navigate to the project directory:
   cd order-book
3. Build the project using Maven:
   mvn clean install
   The application jar file will be created under /target directory.

## Usage
========

From the project directory, run the application with the following command:
java -jar target/order-book-1.0.0.jar

The application logs will be generated in the same folder from where it is run.

Swagger Api: http://localhost:8080/oms/swagger-ui/index.html
OpenApi docs: http://localhost:8080/oms/v3/api-docs

## Configuration
================

There is no additional configuration required for this application.






