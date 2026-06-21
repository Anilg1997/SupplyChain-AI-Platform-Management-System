FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/mcp-service-*.jar app.jar
EXPOSE 8110
ENTRYPOINT ["java", "-jar", "app.jar"]