FROM eclipse-temurin:19-jre-focal

COPY target/teiler-core.jar /app/

WORKDIR /app

RUN apt-get update && apt-get upgrade -y

CMD ["java", "-jar", "teiler-core.jar"]
