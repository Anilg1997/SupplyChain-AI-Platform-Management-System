FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/admin-service-*.jar app.jar
EXPOSE 8111
ENTRYPOINT ["java", "-jar", "app.jar"]