# 1. Etapa de compilación (Usa Gradle y Java 17)
FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
# Construye el empaquetado del servidor
RUN ./gradlew installDist --no-daemon

# 2. Etapa de producción (Servidor ultraligero)
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia solo los archivos necesarios de la etapa anterior
COPY --from=build /app/build/install/ /app/

# Expone el puerto de Render
EXPOSE 8080

# Busca automáticamente tu script de arranque en la carpeta bin y lo ejecuta
CMD sh -c "find /app -path '*/bin/*' -type f ! -name '*.bat' -exec {} \\;"