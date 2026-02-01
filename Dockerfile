# ==========================================
# Dockerfile optimizado para Render
# ==========================================

# Etapa 1: Construcción
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Copiar archivos de configuración de Gradle primero (cache de dependencias)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Dar permisos y descargar dependencias (se cachean si no cambian)
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# Copiar el código fuente
COPY src ./src

# Construir el JAR
RUN ./gradlew clean bootJar --no-daemon -x test

# Etapa 2: Ejecución (imagen más ligera)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Copiar el JAR generado
COPY --from=builder /app/build/libs/*.jar app.jar

# Cambiar al usuario no-root
RUN chown -R appuser:appgroup /app
USER appuser

# Render asigna el puerto dinámicamente via variable PORT
ENV PORT=8080
EXPOSE ${PORT}

# Configuración de JVM optimizada para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Health check para Render
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

# Usar shell form para expandir variables de entorno
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar"]
