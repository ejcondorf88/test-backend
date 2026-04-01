# Multi-stage build para Spring Boot 3 con Java 17
# Stage 1: Build - Compilación con Maven
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

# Directorio de trabajo
WORKDIR /app

# Copiar solo pom.xml primero para aprovechar cache de dependencias
COPY pom.xml .

# Descargar dependencias (esta capa se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar y empaquetar (sin ejecutar tests para velocidad)
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime - Imagen final mínima con JRE
FROM eclipse-temurin:17-jre-alpine

# Crear usuario no-root para seguridad
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -s /bin/sh -D appuser

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR desde el stage de build
COPY --from=builder /app/target/*.jar app.jar

# Cambiar propiedad al usuario no-root
RUN chown -R appuser:appgroup /app

# Cambiar al usuario no-root
USER appuser

# Exponer el puerto de la aplicación
EXPOSE 8081

# Health check básico

# Entrypoint con exec para recibir señales correctamente
ENTRYPOINT ["sh", "-c", "exec java -jar app.jar"]