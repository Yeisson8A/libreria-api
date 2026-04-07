# ===== STAGE 1: BUILD =====
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar pom primero (mejora cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar proyecto
RUN mvn clean package -DskipTests

# ===== STAGE 2: RUN =====
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar jar desde stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Ejecutar app
ENTRYPOINT ["java", "-jar", "app.jar"]