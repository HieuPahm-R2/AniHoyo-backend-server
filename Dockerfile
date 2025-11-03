# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . /app
# Skip tests during build
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-slim  
WORKDIR /app
EXPOSE 8083
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
