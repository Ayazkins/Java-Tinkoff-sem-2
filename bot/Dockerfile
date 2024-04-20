FROM eclipse-temurin:21
WORKDIR /app
COPY target/bot.jar /app
ENV TOKEN=${TOKEN}
EXPOSE 8090
CMD ["java", "-jar", "-D", "app.telegram-token=$TOKEN", "target/bot.jar"]
