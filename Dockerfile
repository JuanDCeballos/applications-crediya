# Etapa 1: Build con Gradle
FROM gradle:8-jdk17 AS builder
WORKDIR /home/gradle/project

# Copiamos solo el microservicio
COPY . .

# Compilamos
RUN gradle bootJar --no-daemon

# Etapa 2: Imagen final
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /home/gradle/project/applications/app-service/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
