# -------- Build stage --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

COPY src ./src
RUN ./mvnw -q -DskipTests package && \
    cp target/*.jar /workspace/app.jar

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]
