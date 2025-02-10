FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar stats-server.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/stats-server.jar"]