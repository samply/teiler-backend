FROM eclipse-temurin:21-jre

COPY target/teiler-backend.jar /app/

WORKDIR /app

CMD ["java", "-jar", "teiler-backend.jar"]
