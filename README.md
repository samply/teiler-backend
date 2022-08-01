# Teiler Core
This service is the backend of the teiler-ui.

# Building
mvn clean package

## Running
java -jar teiler-core.jar
-DAPPLICATION_PORT=
-DNNGM_URL=
-DQUALITY_REPORT_URL=

## Environment variables
APPLICATION_PORT: Port
[OPTIONAL] NNGM_URL: url of nNGM service
[OPTIONAL] QUALITY_REPORT_URL: url of quality report service
