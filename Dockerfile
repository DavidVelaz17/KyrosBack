# syntax=docker/dockerfile:1

# ---- build stage ----
# Se compila con el JDK completo; esta etapa nunca llega a la imagen final, así que su peso
# no importa. `mvnw` se auto-descarga el jar del wrapper si falta (está en .gitignore a propósito).
# Nota: eclipse-temurin *-alpine no publica build para arm64 (Apple Silicon); estas variantes
# basadas en Ubuntu sí son multi-arquitectura (arm64 y amd64).
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copiar solo lo necesario para resolver dependencias primero: si el pom.xml no cambió, Docker
# reusa esta capa cacheada aunque sí haya cambiado el código fuente, acelerando builds repetidos.
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B package -DskipTests

# ---- runtime stage ----
# Imagen final: solo JRE (no JDK) + el jar ya compilado. Sin Maven, sin código fuente.
FROM eclipse-temurin:17-jre
WORKDIR /app

# No correr como root dentro del contenedor.
RUN groupadd -r spring && useradd -r -g spring spring

COPY --from=build /app/target/*.jar app.jar

# Carpeta de fotos de alumnos: se monta como volumen con nombre desde docker-compose para que
# sobreviva a reconstrucciones de la imagen (ver UPLOADS_DIR en application.properties).
RUN mkdir -p /app/uploads && chown -R spring:spring /app

USER spring
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
