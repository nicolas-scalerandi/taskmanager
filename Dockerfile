# --- Fase 1: Compilación ---
# Usamos Maven con Eclipse Temurin y Java 17 exacto
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el archivo pom.xml para descargar las dependencias y aprovechar la caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo JAR omitiendo los tests para acelerar el proceso
COPY src ./src
RUN mvn clean package -DskipTests

# --- Fase 2: Imagen de ejecución ligera ---
# Usamos un JRE ligero de Java 17 sobre una base Ubuntu optimizada (Jammy)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiar el JAR generado desde la fase de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de red de Spring Boot
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]