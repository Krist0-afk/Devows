FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8094

ENTRYPOINT ["java", "-jar", "app.jar"]