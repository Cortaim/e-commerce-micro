# Используем официальный образ Java
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл jar в контейнер	
COPY target/order-service-0.0.1-SNAPSHOT.jar order-service.jar

# Открываем порт 8082 для доступа
EXPOSE 8082

# Команда для запуска сервиса
ENTRYPOINT ["java", "-jar", "order-service.jar"]