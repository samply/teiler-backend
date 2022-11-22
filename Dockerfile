FROM amazoncorretto:17.0.5-alpine

COPY target/teiler-core.jar /app/

WORKDIR /app

CMD ["java", "-jar", "teiler-core.jar"]
