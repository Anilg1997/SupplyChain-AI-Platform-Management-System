FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/ai-rag-service-*.jar app.jar
EXPOSE 8109
ENTRYPOINT ["java", "-jar", "app.jar"]