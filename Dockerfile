# -------- Build stage --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copiamos wrapper y POM primero para cachear dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Ahora el código fuente
COPY src ./src
RUN ./mvnw -q -DskipTests package

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el jar final (comodín funciona con spring-boot:repackage)
COPY --from=build /workspace/target/*.jar app.jar

# Render inyecta $PORT; en application.properties usaremos server.port=${PORT:8080}
EXPOSE 8080

# Puedes setear JAVA_TOOL_OPTIONS en Render (memoria/GC)
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]
