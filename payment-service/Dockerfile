# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем артефакт .jar в контейнер
COPY target/payment-service-0.0.1-SNAPSHOT.jar /app/payment-service.jar

# Открываем порт 8080 для связи с контейнером
EXPOSE 8083

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/payment-service.jar"]
