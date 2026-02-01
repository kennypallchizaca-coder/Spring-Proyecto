# Etapa 1: Construcción
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY . .
# Dar permisos de ejecución al wrapper y construir
RUN chmod +x gradlew
RUN ./gradlew clean bootJar

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copiar el JAR generado (el nombre puede variar, usamos wildcard)
COPY --from=builder /app/build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
