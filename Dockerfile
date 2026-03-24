FROM maven:3.9-eclipse-temurin-21-alpine AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine 

COPY --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]

