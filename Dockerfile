## Stage 1: Build the application
#FROM openjdk:22-oraclelinux8
#
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#COPY src ./src
#
#RUN ./mvnw dependency:go-offline
#CMD ["./mvnw", "spring-boot:run"]
