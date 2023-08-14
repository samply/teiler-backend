FROM eclipse-temurin:20-jre

COPY target/teiler-core.jar /app/

WORKDIR /app

RUN apt-get update && apt-get upgrade -y

CMD ["java", "-jar", "teiler-core.jar"]
