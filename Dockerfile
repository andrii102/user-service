# Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies first to speed up subsequent builds
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

#Create the lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a directory for the H2 database files
RUN mkdir /app/data

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]