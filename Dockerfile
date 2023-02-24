FROM eclipse-temurin:19-jre-alpine

COPY target/teiler-core.jar /app/

WORKDIR /app

RUN apk upgrade

CMD ["java", "-jar", "teiler-core.jar"]
