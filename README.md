# Teiler Core
This service is the backend of the teiler-dashboard.

# Building
mvn clean package

## Running
java -jar teiler-backend.jar
-DAPPLICATION_PORT=

## Environment variables
APPLICATION_PORT: Port
[OPTIONAL] TODO

## Docker
docker build -t teiler-backend .
docker run teiler-backend -p 8085:80
