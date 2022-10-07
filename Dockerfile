FROM amazoncorretto:17.0.4-alpine3.16

COPY target/teiler-core.jar /app/

WORKDIR /app

CMD ["java", "-jar", "teiler-core.jar"]
