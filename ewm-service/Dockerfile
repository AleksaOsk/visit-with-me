FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar evm-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/evm-service.jar"]