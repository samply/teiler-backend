# Teiler Core
This service is the backend of the teiler-ui.

# Building
mvn clean package

## Running
java -jar teiler-core.jar
-DAPPLICATION_PORT=

## Environment variables
APPLICATION_PORT: Port
[OPTIONAL] TODO

## Docker
docker build -t teiler-core .
docker run teiler-core -p 8085:80
