# Build with Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy and build without tests
COPY . .
RUN mvn clean package -DskipTests

# Run with Java 21
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built .jar
COPY --from=build /app/target/*.jar app.jar

# Open port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
