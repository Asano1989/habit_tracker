# ---------- build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY app/pom.xml .
RUN mvn dependency:go-offline -B

COPY app/ .
RUN mvn clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/habit_tracker-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]