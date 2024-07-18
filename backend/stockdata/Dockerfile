FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/stockdata-1.0.0.jar stockdata-1.0.0.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "stockdata-1.0.0.jar"]