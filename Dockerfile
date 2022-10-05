FROM openjdk:17-oracle

RUN microdnf upgrade && microdnf remove expat fontconfig freetype \
  aajohan-comfortaa-fonts fontpackages-filesystem gzip bzip2 tar libpng \
  binutils && microdnf clean all

COPY target/teiler-core.jar /app/

WORKDIR /app

CMD ["java", "-jar", "teiler-core.jar"]
