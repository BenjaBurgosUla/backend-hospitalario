# 1. Etapa de compilación (Usa Gradle y Java 21)
FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
# Otorga permisos de ejecución al script de Gradle
RUN chmod +x ./gradlew
# Construye el empaquetado del servidor
RUN ./gradlew installDist --no-daemon

# 2. Etapa de producción (Usa Eclipse Temurin con Java 21)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copia solo los archivos necesarios de la etapa anterior
COPY --from=build /app/build/install/ /app/

# Expone el puerto de Render
EXPOSE 8080

# Busca automáticamente tu script de arranque en la carpeta bin y lo ejecuta
CMD sh -c "find /app -path '*/bin/*' -type f ! -name '*.bat' -exec {} \\;"